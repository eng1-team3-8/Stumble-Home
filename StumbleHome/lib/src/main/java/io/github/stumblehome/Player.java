package io.github.stumblehome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
public class Player extends Sprite {
    // Spritesheet containing all player animation frames.
    public Texture characterSheet;

    // Map of all animations with key of animation name
    private Map<Animation_enum, Animation<TextureRegion>> animations;

    // Currently active animation being played.
    private Animation<TextureRegion> currentAnimation;

    // Previous animation, used to reset timing when animation changes.
    private Animation<TextureRegion> previousAnimation;

    // Time elapsed in the current animation.
    private float stateTime;

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

    // The direction the player was last moving or facing.
    private Animation_enum lastDirection = Animation_enum.DOWN;

    // Current x position of the player on the map.
    public float playerX;

    // Current y position of the player on the map.
    public float playerY;

    // Size of the player's collision box and sprite.
    public final float playerSize = 0.8f;

    // Delta time value for consistent movement speed.
    private final float delta = Gdx.graphics.getDeltaTime();

    // Controls whether the player has reversed controls (1 = drunk, 0 = sober).
    public boolean isDrunk = true;

    // Width of the game map in tiles.
    private final float mapWidth;

    // Height of the game map in tiles.
    private final float mapHeight;

    // Speed of the player
    public float player_speed;

    // Boolean to see if the player can get drunk (default true, only false after a meal)
    public boolean canGetDrunk = true;

    // Layer containing collision information from the tiled map.
    TiledMapTileLayer collisionLayer;

    /**
     * Constructs a new player with the given sprite, map boundaries and layer which contains
     * hedges in the map. Also calls method to initialize animations.
     *
     * @param sprite the sprite containing the player texture
     * @param mapWidth width of the game map
     * @param mapHeight height of the game map
     * @param collisionLayer the tile layer used for collision detection
     */
    public Player(
            Sprite sprite, float mapWidth, float mapHeight, TiledMapTileLayer collisionLayer) {
        super(sprite);

        // Initialize player animations
        initializeAnimations();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.player_speed = 5f;
        this.isDrunk = false;

        // Intialises as normal for understandability and then gets player drunk
        SwapControls();

        this.collisionLayer = collisionLayer;
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

        characterSheet = this.getTexture();
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

        stateTime = 0f;
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

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            // RIGHT key -> move LEFT
            moveX = player_speed * delta;
            currentAnimation = animations.get(Animation_enum.WALK_RIGHT);
            lastDirection = Animation_enum.LEFT;
            moving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.A)) {
            // LEFT key -> move RIGHT
            moveX = -player_speed * delta;
            currentAnimation = animations.get(Animation_enum.WALK_LEFT);
            lastDirection = Animation_enum.RIGHT;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            // UP key -> move DOWN
            moveY = player_speed * delta;
            currentAnimation = animations.get(Animation_enum.WALK_UP);
            lastDirection = Animation_enum.DOWN;
            moving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
                || Gdx.input.isKeyPressed(Input.Keys.S)) {
            // DOWN key -> move UP
            moveY = -player_speed * delta;
            currentAnimation = animations.get(Animation_enum.WALK_DOWN);
            lastDirection = Animation_enum.UP;
            moving = true;
        }

        if (moveX != 0 && canMoveTo(playerX + moveX, playerY)) {
            playerX += moveX;
        }
        if (moveY != 0 && canMoveTo(playerX, playerY + moveY)) {
            playerY += moveY;
        }

        if (!moving) {
            currentAnimation = animations.get(lastDirection);
        }
        clampPlayerPosition();
    }

    /**
     * Keeps the player within map boundaries by constraining position values.
     */
    private void clampPlayerPosition() {
        playerX = MathUtils.clamp(playerX, 0, mapWidth - playerSize);
        playerY = MathUtils.clamp(playerY, 0, mapHeight - playerSize);
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
                && isTileBlocked(x + playerSize, y)
                && isTileBlocked(x, y + playerSize)
                && isTileBlocked(x + playerSize, y + playerSize);
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
        if (currentAnimation != previousAnimation) {
            stateTime = 0f;
            previousAnimation = currentAnimation;
        }

        // Update animation time
        stateTime += Gdx.graphics.getDeltaTime();
    }

    /**
     * Renders the player character using the appropriate animation frame or standing pose.
     *
     * @param batch the sprite batch to draw with
     */
    public void draw(SpriteBatch batch) {
        // Determine which frame to draw
        TextureRegion frameToDraw;
        frameToDraw = currentAnimation.getKeyFrame(stateTime, true);

        // Draw the character with proper aspect ratio
        // Frames are 25x49 pixels (width x height), aspect ratio = 49/25 = 1.96
        float aspectRatio = 49f / 25f;
        float drawHeight = playerSize * aspectRatio;
        batch.draw(frameToDraw, playerX, playerY, playerSize, drawHeight);
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
}
