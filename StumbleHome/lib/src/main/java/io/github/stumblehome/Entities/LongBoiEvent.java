package io.github.stumblehome.Entities;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/** Handles the Long Boi event movement, animations, and interactions with the game world. */
public class LongBoiEvent extends NonPlayerEntity {
    public static final String ASSET = "longBoi.png";

    // Size of longBoi collision box
    private final float longBoxWidth = 0.65f;
    private final float longBoxHeight = 1.024f;
    // Offset to centre the collision box
    private final float longBoxOffsetX;
    private final float longBoxOffsetY = 0f;

    // Variable which is true when long boi has completed the walk.
    public boolean done_walk = false;
    // Variable which is true when player collided with long boi.
    public boolean collided = false;
    // Distance between player and long boi to set near to true.
    private float radius = 6;
    // Layer containing collision information from the tiled map.
    private TiledMapTileLayer collision_layer;
    // Previous movement storage vars
    String prev_move_LR = ""; // Left / right
    String prev_move_UD = ""; // Up / down

    // Variable which is true when player is in radius of long boi.
    private boolean near = false;
    private float speed = 2f;

    /**
     * Constructs a new longBoiEvent with given sprite and initialises animations.
     *
     * @param sprite the sprite containing long boi texture.
     */
    public LongBoiEvent(
            Texture texture, float size, float[] position, TiledMapTileLayer collision) {
        super(texture, size, position, 32, 32, 2);

        this.collision_layer = collision;
        longBoxOffsetX = (frame_size - longBoxWidth) / 2f;
        // initialize position from the provided sprite (keeps the sprite where caller placed it)
        this.setX(position[0]);
        this.setY(position[1]);
    }

    /**
     * Updates the animation state timer each frame. Resets the timer when switching between different
     * animations. Only does these if player is near long boi.
     */
    public void logic() {
        if (near) {
            // Reset animation time if animation changed
            if (current_animation != previous_animation) {
                state_time = 0f;
                previous_animation = current_animation;
            }

            // Update animation time
            state_time += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Renders long boi to the screen if near and not finished walking.
     *
     * @param batch the sprite batch used for rendering.
     */
    @Override
    public void draw(Batch batch) {
        TextureRegion frame_to_draw;
        if (near & !done_walk) {
            frame_to_draw = current_animation.getKeyFrame(state_time, true);
            // draw long boi
            batch.draw(frame_to_draw, this.getX(), this.getY(), frame_size, frame_size);
        }
    }

    /**
     * Checks whether player's centre is within 1.3 world units of long boi's centre. If so, variable
     * 'collided' is set to true.
     *
     * @param playerCentreX the centre of the player in x direction.
     * @param playerCentreY the centre of player in y direction.
     * @return true if player has collided with long boi
     */
    @Override
    public boolean checkColliding(float playerCentreX, float playerCentreY) {
        if (collided) {
            return true;
        }

        boolean colliding;
        // calculate distance between player centre and long boi centre using pythagoras
        double distance =
                sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        colliding = distance < 1.3f;

        if (colliding) {
            collided = true;
        }
        return colliding;
    }

    /**
     * Checks whether player's centre is within the radius of long boi's centre. If so, variable
     * 'near' is set to true.
     *
     * @param playerCentreX the centre of the player in x direction.
     * @param playerCentreY the centre of player in y direction.
     * @return true if player is near long boi in that frame, false if player is already near or not
     *     within radius.
     */
    public boolean checkNear(float playerCentreX, float playerCentreY) {
        if (near) {
            return false;
        }

        boolean nearing;
        // calculate distance between player centre and long boi centre using pythagoras
        double distance =
                sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        nearing = distance < radius;

        if (nearing) {
            near = true;
        }
        return nearing;
    }

    public boolean getNear() {
        return near;
    }

    /**
     * This controls long boi's movement based on collisions with the maze Defaults to move up and
     * right Reacts to collisions to semi-randomly move around the map The route taken will change a
     * little on each run
     */
    public void walkPath() {
        float delta = Gdx.graphics.getDeltaTime();

        // New collision based movement algorithm
        // Moves long boi, uses previous move check to maintain direction switch on collision
        if (this.canMoveTo(this.getX() + delta * speed, this.getY())
                && !prev_move_LR.equals("left")) {
            this.setX(this.getX() + delta * speed);
            prev_move_LR = "right";
        } else if (this.canMoveTo(this.getX() - delta * speed, this.getY())
                && !prev_move_LR.equals("right")) {
            this.setX(this.getX() - delta * speed);
            prev_move_LR = "left";
        } else {
            prev_move_LR = "";
        }

        // Same as above but for up/down
        if (this.canMoveTo(this.getX(), this.getY() + delta * speed)
                && !prev_move_UD.equals("down")) {
            this.setY(this.getY() + delta * speed);
            prev_move_UD = "up";
        } else if (this.canMoveTo(this.getX(), this.getY() - delta * speed)
                && !prev_move_UD.equals("up")) {
            this.setY(this.getY() - delta * speed);
            prev_move_UD = "down";
        } else {
            prev_move_UD = "";
        }
    }

    /**
     * Checks if the Long Boi can move to a target position without hitting obstacles. Validates all
     * four corners of the sprite's hitbox.
     *
     * @param x the target x position
     * @param y the target y position
     * @return true if the move is valid, false if blocked
     */
    private boolean canMoveTo(float x, float y) {
        float offsetX = x + longBoxOffsetX;
        float offsetY = y + longBoxOffsetY;
        return isTileBlocked(offsetX, offsetY)
                && isTileBlocked(offsetX + longBoxWidth, offsetY)
                && isTileBlocked(offsetX, offsetY + longBoxHeight)
                && isTileBlocked(offsetX + longBoxWidth, offsetY + longBoxHeight);
    }

    /**
     * Determines whether a specific tile position allows the sprite to pass through. Handles special
     * cases for certain tile types that have partial collision.
     *
     * @param x the x coordinate to check
     * @param y the y coordinate to check
     * @return true if passable, false if blocked
     */
    private boolean isTileBlocked(float x, float y) {
        int tileX = (int) x;
        int tileY = (int) y;

        TiledMapTileLayer.Cell cell = collision_layer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return true;

        int tileId = cell.getTile().getId();

        if (tileId == 5) return true;

        if (tileId == 2
                || tileId == 3
                || tileId == 4
                || tileId == 19
                || tileId == 20
                || tileId == 21
                || tileId == 22) {
            float yInTile = y - tileY;
            return !(yInTile < 0.4f);
        }

        return false;
    }

    /**
     * setter for longBoi speed. can be negative to make long boi go backwards
     * @param speed new speed of longboi
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * getter for the radius that long boi will treat as near
     * @return the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * setter for the radius
     * @param radius to be set, if negative is turned positive as positive radius is equivalent but awkward
     */
    public void setRadius(float radius) {
        if (radius < 0) {
            radius *= -1;
        } else {
            this.radius = radius;
        }
    }
}
