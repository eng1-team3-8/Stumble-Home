package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Handles the Long Boi event movement, animations, and interactions with the game world.
 */
public class longBoiEvent extends Sprite {
    // Sprite sheet containing all animation frames.
    public Texture longSheet;

    // Currently active animation being played.
    private Animation<TextureRegion> currentAnimation;

    // Previous animation, used to reset timing when animation changes.
    private Animation<TextureRegion> previousAnimation;

    // Time elapsed in the current animation.
    private float stateTime;

    // Current x position of long boi on map.
    public float longX;

    // Current y position of long boi on map.
    public float longY;

    // Scale of long boi frames.
    public final float longSize = 2f;

    // Size of longBoi collision box
    private final float longBoxWidth = 0.65f;
    private final float longBoxHeight = 1.024f;

    // Offset to centre the collision box
    private final float longBoxOffsetX = (longSize - longBoxWidth) / 2f;
    private final float longBoxOffsetY = 0f;

    // Variable which is true when player is in radius of long boi.
    private boolean near = false;

    // Variable which is true when long boi has completed the walk.
    public boolean doneWalk = false;

    // Variable which is true when player collided with long boi.
    public boolean collided = false;

    // Distance between player and long boi to set near to true.
    public float radius = 6;

    // Layer containing collision information from the tiled map.
    TiledMapTileLayer collisionLayer;

    // Previous movement storage vars
    String prev_move_LR = ""; // Left / right
    String prev_move_UD = ""; // Up / down

    // Width of the game map in tiles.
    private float mapWidth;

    // Height of the game map in tiles.
    private float mapHeight;
    public static final String ASSET = "longBoi.png";


    /**
     * Constructs a new longBoiEvent with given sprite and initialises animations.
     *
     * @param sprite the sprite containing long boi texture.
     */
    public longBoiEvent(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);

        // initialize animations
        initializeAnimations();
        this.collisionLayer =  collisionLayer;

        // initialize map dimensions from the collision layer so clamping has valid bounds
        this.mapWidth = collisionLayer.getWidth();
        this.mapHeight = collisionLayer.getHeight();

        // initialize position from the provided sprite (keeps the sprite where caller placed it)
        this.longX = sprite.getX();
        this.longY = sprite.getY();
    }

    /**
     * Loads all animation frames from the long boi spritesheet.
     */
    private void initializeAnimations() {
        int frameWidth = 32;
        int frameHeight = 32;

        longSheet = this.getTexture();

        // set idle frame
        //idleFrame = new TextureRegion(longSheet, 0, 32, frameWidth, frameHeight);

        // walking frames
        TextureRegion[] longFrames = new TextureRegion[2];
        longFrames[0] = new TextureRegion(longSheet, 0, 0, frameWidth, frameHeight);
        longFrames[1] = new TextureRegion(longSheet, 32, 0, frameWidth, frameHeight);
        Animation<TextureRegion> animation = new Animation<>(0.3f, longFrames);

        stateTime = 0f;

        currentAnimation = animation;
    }

    /**
     * Updates the animation state timer each frame. Resets the timer when switching between
     * different animations. Only does these if player is near long boi.
     */
    public void logic() {
        if (near) {
            // Reset animation time if animation changed
            if (currentAnimation != previousAnimation) {
                stateTime = 0f;
                previousAnimation = currentAnimation;
            }

            // Update animation time
            stateTime += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Renders long boi to the screen if near and not finished walking.
     *
     * @param batch the sprite batch used for rendering.
     */
    public void draw(SpriteBatch batch) {
        TextureRegion frameToDraw;
        if (near & !doneWalk) {
            frameToDraw = currentAnimation.getKeyFrame(stateTime, true);
            // draw long boi
            batch.draw(frameToDraw, longX, longY, longSize, longSize);
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
    public boolean checkCollision(float playerCentreX, float playerCentreY) {
        if (collided) {
            return true;
        }

        boolean colliding;
        //calculate distance between player centre and long boi centre using pythagoras
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

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
     * @return true if player is near long boi in that frame, false if player is already near or not within radius.
     */
    public boolean checkNear(float playerCentreX, float playerCentreY) {
        if (near) {
            return false;
        }

        boolean nearing;
        //calculate distance between player centre and long boi centre using pythagoras
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        nearing = distance < radius;

        if (nearing) {
            near = true;
        }
        return nearing;
    }

    private float getCentreX(){
        return longX + longSize / 2;
    }

    private float getCentreY(){
        return longY + longSize / 2;
    }

    public boolean getNear() {
        return near;
    }

    /**
     * This controls long boi's movement based on collisions with the maze
     * Defaults to move up and right
     * Reacts to collisions to semi-randomly move around the map
     * The route taken will change a little on each run
     */
    public void walkPath(){
        float delta = Gdx.graphics.getDeltaTime();
        float speed = 3f;

        // New collision based movement algorithm
        // Moves long boi, uses previous move check to maintain direction switch on collision
        if (this.canMoveTo(longX + delta*speed, longY) && !prev_move_LR.equals("left")){
            longX += delta*speed;
            prev_move_LR = "right";
        }
        else if (this.canMoveTo(longX - delta*speed, longY) && !prev_move_LR.equals("right")){
            longX -= delta*speed;
            prev_move_LR = "left";
        }
        else{
            prev_move_LR = "";
        }

        // Same as above but for up/down
        if (this.canMoveTo(longX, longY + delta*speed) && !prev_move_UD.equals("down")){
            longY += delta*speed;
            prev_move_UD = "up";
        }
        else if (this.canMoveTo(longX, longY - delta*speed) && !prev_move_UD.equals("up")){
            longY -= delta*speed;
            prev_move_UD = "down";
        }
        else{
            prev_move_UD = "";
        }
    }

    /**
     * Checks if the Long Boi can move to a target position without hitting obstacles.
     * Validates all four corners of the sprite's hitbox.
     *
     * @param x the target x position
     * @param y the target y position
     * @return true if the move is valid, false if blocked
     */
    private boolean canMoveTo(float x, float y) {
        float offsetX = x + longBoxOffsetX;
        float offsetY = y + longBoxOffsetY;
        return isTileBlocked(offsetX, offsetY) &&
            isTileBlocked(offsetX + longBoxWidth, offsetY) &&
            isTileBlocked(offsetX, offsetY + longBoxHeight) &&
            isTileBlocked(offsetX + longBoxWidth, offsetY + longBoxHeight);
    }

    /**
     * Determines whether a specific tile position allows the sprite to pass through.
     * Handles special cases for certain tile types that have partial collision.
     *
     * @param x the x coordinate to check
     * @param y the y coordinate to check
     * @return true if passable, false if blocked
     */
    private boolean isTileBlocked(float x, float y) {
        int tileX = (int) x;
        int tileY = (int) y;

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return true;

        int tileId = cell.getTile().getId();

        if (tileId == 5) return true;

        if (tileId == 2 || tileId == 3 || tileId == 4 || tileId == 19 || tileId == 20 || tileId == 21 || tileId == 22) {
            float yInTile = y - tileY;
            return !(yInTile < 0.4f);
        }

        return false;
    }
}
