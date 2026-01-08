package io.github.stumblehome.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the player character's movement, animations, and interactions with the game world.
 * Supports both normal and reversed controls depending on the player's state.
 */
public class Player extends Entity {
    public static final String ASSET = "character.png";

    // Delta time value for consistent movement speed.
    private float delta = Gdx.graphics.getDeltaTime();
    // Width of the game map in tiles.
    private final float mapWidth;
    // Height of the game map in tiles.
    private final float mapHeight;
    // Controls whether the player has reversed controls (1 = drunk, 0 = sober).
    public boolean isDrunk = true;
    // Speed of the player
    public float player_speed;
    // Boolean to see if the player can get drunk (default true, only false after a meal)
    public boolean canGetDrunk = true;
    // Layer containing collision information from the tiled map.
    TiledMapTileLayer collisionLayer;
    // Map of all animations with key of animation name
    private Map<Animation_enum, Animation<TextureRegion>> animations;
    // The direction the player was last moving or facing.
    private Animation_enum lastDirection = Animation_enum.DOWN;

    public Player(
            Texture texture,
            float size,
            float[] position,
            float mapWidth,
            float mapHeight,
            TiledMapTileLayer collisionLayer) {
        super(texture, size, position);

        // Initialize player animations
        initializeAnimations();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.player_speed = 5f;
        this.isDrunk = false;

        // Intialises as normal for understandability and then gets player drunk
        SwapControls();

        this.collisionLayer = collisionLayer;
        System.out.println(this.canMoveTo(2f, 2f));
    }

    /**
     * Loads all animation frames from the character spritesheet.
     * Sets up walking animations for all four directions plus standing poses.
     */
    private void initializeAnimations() {
        // Frame size: 25x49 pixels
        final int FRAME_WIDTH = 25;
        final int FRAME_HEIGHT = 49;

        // 4 walking frames
        final int NUM_WALK_FRAMES = 4;

        // 1 standing frame
        final int NUM_STAND_FRAMES = 1;

        animations = new HashMap<Animation_enum, Animation<TextureRegion>>();

        Texture characterSheet = this.getTexture();
        TextureRegion[] walk_frames = new TextureRegion[NUM_WALK_FRAMES];
        TextureRegion[] stand_frames = new TextureRegion[NUM_STAND_FRAMES];

        // Standing poses (static, not animated)
        stand_frames[0] = new TextureRegion(characterSheet, 4, 10, FRAME_WIDTH, FRAME_HEIGHT);
        animations.put(
                Animation_enum.RIGHT, new Animation<TextureRegion>(0.1f, stand_frames.clone()));

        stand_frames[0] = new TextureRegion(characterSheet, 198, 9, FRAME_WIDTH, FRAME_HEIGHT);
        animations.put(
                Animation_enum.LEFT, new Animation<TextureRegion>(0.1f, stand_frames.clone()));

        stand_frames[0] = new TextureRegion(characterSheet, 3, 65, FRAME_WIDTH, FRAME_HEIGHT);
        animations.put(Animation_enum.UP, new Animation<TextureRegion>(0.1f, stand_frames.clone()));

        stand_frames[0] = new TextureRegion(characterSheet, 3, 120, FRAME_WIDTH, FRAME_HEIGHT);
        animations.put(
                Animation_enum.DOWN, new Animation<TextureRegion>(0.1f, stand_frames.clone()));

        // Walking LEFT (4 frames)
        walk_frames[0] = new TextureRegion(characterSheet, 44, 9, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[1] = new TextureRegion(characterSheet, 78, 10, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[2] = new TextureRegion(characterSheet, 117, 9, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[3] = new TextureRegion(characterSheet, 151, 10, FRAME_WIDTH, FRAME_HEIGHT);
        animations.put(Animation_enum.WALK_LEFT, new Animation<>(0.1f, walk_frames.clone()));

        // Walking RIGHT (4 frames)
        walk_frames[0] = new TextureRegion(characterSheet, 198, 64, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[1] = new TextureRegion(characterSheet, 230, 65, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[2] = new TextureRegion(characterSheet, 262, 65, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[3] = new TextureRegion(characterSheet, 292, 65, FRAME_WIDTH, FRAME_HEIGHT);
        animations.put(Animation_enum.WALK_RIGHT, new Animation<>(0.1f, walk_frames.clone()));

        // Walking FORWARD/DOWN (4 frames)
        walk_frames[0] = new TextureRegion(characterSheet, 43, 64, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[1] = new TextureRegion(characterSheet, 79, 66, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[2] = new TextureRegion(characterSheet, 115, 65, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[3] = new TextureRegion(characterSheet, 151, 66, FRAME_WIDTH, FRAME_HEIGHT);
        animations.put(Animation_enum.WALK_DOWN, new Animation<>(0.1f, walk_frames.clone()));

        // Walking BACKWARDS/UP (4 frames)
        walk_frames[0] = new TextureRegion(characterSheet, 43, 121, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[1] = new TextureRegion(characterSheet, 78, 122, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[2] = new TextureRegion(characterSheet, 115, 121, FRAME_WIDTH, FRAME_HEIGHT);
        walk_frames[3] = new TextureRegion(characterSheet, 152, 122, FRAME_WIDTH, FRAME_HEIGHT);
        animations.put(Animation_enum.WALK_UP, new Animation<>(0.1f, walk_frames));

        state_time = 0f;
    }

    /**
     * swaps the controls, intended for when player becomes drunk/sober
     */
    public void SwapControls() {
        Animation<TextureRegion> swap_placeholder = animations.get(Animation_enum.WALK_UP);
        animations.put(Animation_enum.WALK_UP, animations.get(Animation_enum.WALK_DOWN));
        animations.put(Animation_enum.WALK_DOWN, swap_placeholder);
        swap_placeholder = animations.get(Animation_enum.WALK_LEFT);
        animations.put(Animation_enum.WALK_LEFT, animations.get(Animation_enum.WALK_RIGHT));
        animations.put(Animation_enum.WALK_RIGHT, swap_placeholder);
        swap_placeholder = animations.get(Animation_enum.UP);
        animations.put(Animation_enum.UP, animations.get(Animation_enum.DOWN));
        animations.put(Animation_enum.DOWN, swap_placeholder);
        swap_placeholder = animations.get(Animation_enum.LEFT);
        animations.put(Animation_enum.LEFT, animations.get(Animation_enum.RIGHT));
        animations.put(Animation_enum.RIGHT, swap_placeholder);

        player_speed = -player_speed;
        if (isDrunk) {
            isDrunk = false;
        } else {
            isDrunk = true;
        }
    }

    /**
     * Processes keyboard input and updates player position based on movement controls.
     * Handles both normal and reversed controls depending on the isDrunk state.
     * Also checks for collisions before allowing movement.
     */
    public void input() {
        float moveX = 0;
        float moveY = 0;
        boolean moving = false;
        delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            // RIGHT key -> move LEFT
            moveX = player_speed * delta;
            current_animation = animations.get(Animation_enum.WALK_RIGHT);
            lastDirection = Animation_enum.LEFT;
            moving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.A)) {
            // LEFT key -> move RIGHT
            moveX = -player_speed * delta;
            current_animation = animations.get(Animation_enum.WALK_LEFT);
            lastDirection = Animation_enum.RIGHT;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            // UP key -> move DOWN
            moveY = player_speed * delta;
            current_animation = animations.get(Animation_enum.WALK_UP);
            lastDirection = Animation_enum.DOWN;
            moving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
                || Gdx.input.isKeyPressed(Input.Keys.S)) {
            // DOWN key -> move UP
            moveY = -player_speed * delta;
            current_animation = animations.get(Animation_enum.WALK_DOWN);
            lastDirection = Animation_enum.UP;
            moving = true;
        }

        if (moveX != 0 && canMoveTo(this.getX() + moveX, this.getY())) {
            this.setX(this.getX() + moveX);
        }
        if (moveY != 0 && canMoveTo(this.getX(), this.getY() + moveY)) {
            this.setY(this.getY() + moveY);
        }

        if (!moving) {
            current_animation = animations.get(lastDirection);
        }
        clampPlayerPosition();
    }

    /**
     * Keeps the player within map boundaries by constraining position values.
     */
    private void clampPlayerPosition() {
        this.setX(MathUtils.clamp(this.getX(), 0, mapWidth - frame_size));
        this.setY(MathUtils.clamp(this.getY(), 0, mapHeight - frame_size));
    }

    /**
     * Checks if the player can move to a target position without hitting obstacles.
     * Validates all four corners of the player's hitbox.
     *
     * @param x the target x position
     * @param y the target y position
     * @return true if the move is valid, false if blocked
     */
    public boolean canMoveTo(float x, float y) {
        return isTileBlocked(x, y)
                && isTileBlocked(x + frame_size, y)
                && isTileBlocked(x, y + frame_size)
                && isTileBlocked(x + frame_size, y + frame_size);
    }

    /**
     * Determines whether a specific tile position allows the player to pass through.
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
     * Updates the animation state timer each frame.
     * Resets the timer when switching between different animations.
     */
    public void logic() {
        // Reset animation time if animation changed
        if (current_animation != previous_animation) {
            state_time = 0f;
            previous_animation = current_animation;
        }

        // Update animation time
        state_time += Gdx.graphics.getDeltaTime();
    }

    /**
     * Renders the player character using the appropriate animation frame or standing pose.
     *
     * @param batch the sprite batch to draw with
     */
    public void draw(SpriteBatch batch) {
        // Determine which frame to draw
        TextureRegion frameToDraw;
        frameToDraw = current_animation.getKeyFrame(state_time, true);

        // Draw the character with proper aspect ratio
        // Frames are 25x49 pixels (width x height), aspect ratio = 49/25 = 1.96
        float aspectRatio = 49f / 25f;
        float drawHeight = frame_size * aspectRatio;
        batch.draw(frameToDraw, this.getX(), this.getY(), frame_size, drawHeight);
    }

    public void slowDownPlayer(float amount) {
        if (isDrunk) {
            amount *= -1;
        }
        this.player_speed -= amount;
    }

    public void speedUpPlayer(float amount) {
        if (isDrunk) {
            amount *= -1;
        }
        this.player_speed += amount;
    }

    public EntityDirection getDirection() {
        if (isDrunk) {
            if (lastDirection == Animation_enum.DOWN || lastDirection == Animation_enum.WALK_DOWN) {
                return EntityDirection.DOWN;
            }
            if (lastDirection == Animation_enum.UP || lastDirection == Animation_enum.WALK_UP) {
                return EntityDirection.UP;
            }
            if (lastDirection == Animation_enum.RIGHT
                    || lastDirection == Animation_enum.WALK_RIGHT) {
                return EntityDirection.RIGHT;
            }
            if (lastDirection == Animation_enum.LEFT || lastDirection == Animation_enum.WALK_LEFT) {
                return EntityDirection.LEFT;
            }
        } else {
            if (lastDirection == Animation_enum.DOWN || lastDirection == Animation_enum.WALK_DOWN) {
                return EntityDirection.UP;
            }
            if (lastDirection == Animation_enum.UP || lastDirection == Animation_enum.WALK_UP) {
                return EntityDirection.DOWN;
            }
            if (lastDirection == Animation_enum.RIGHT
                    || lastDirection == Animation_enum.WALK_RIGHT) {
                return EntityDirection.LEFT;
            }
            if (lastDirection == Animation_enum.LEFT || lastDirection == Animation_enum.WALK_LEFT) {
                return EntityDirection.RIGHT;
            }
        }
        return null;
    }

    private enum Animation_enum {
        DOWN,
        LEFT,
        RIGHT,
        UP,
        WALK_DOWN,
        WALK_LEFT,
        WALK_RIGHT,
        WALK_UP
    }
}
