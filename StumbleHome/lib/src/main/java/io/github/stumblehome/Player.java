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

/**
 * Handles the player character's movement, animations, and interactions with the game world.
 * Supports both normal and reversed controls depending on the player's state.
 */
public class Player extends Sprite {
    /** Spritesheet containing all player animation frames. */
    public Texture characterSheet;

    /** Walking animation when moving downward. */
    private Animation<TextureRegion> walkDown;

    /** Walking animation when moving left. */
    private Animation<TextureRegion> walkLeft;

    /** Walking animation when moving right. */
    private Animation<TextureRegion> walkRight;

    /** Walking animation when moving upward. */
    private Animation<TextureRegion> walkUp;

    /** Static frame displayed when standing still facing down. */
    private TextureRegion standDown;

    /** Static frame displayed when standing still facing left. */
    private TextureRegion standLeft;

    /** Static frame displayed when standing still facing right. */
    private TextureRegion standRight;

    /** Static frame displayed when standing still facing up. */
    private TextureRegion standUp;

    /** Currently active animation being played. */
    private Animation<TextureRegion> currentAnimation;

    /** Previous animation, used to reset timing when animation changes. */
    private Animation<TextureRegion> previousAnimation;

    /** The static pose to show when player isn't moving. */
    private TextureRegion currentStandingPose;

    /** Time elapsed in the current animation. */
    private float stateTime;

    /** Possible directions the player can face. */
    private enum Direction { DOWN, LEFT, RIGHT, UP }

    /** The direction the player was last moving or facing. */
    private Direction lastDirection = Direction.DOWN;

    /** Current x position of the player on the map. */
    public float playerX;

    /** Current y position of the player on the map. */
    public float playerY;

    /** Size of the player's collision box and sprite. */
    public final float playerSize = 0.8f;

    /** Delta time value for consistent movement speed. */
    private final float delta = Gdx.graphics.getDeltaTime();

    /** Controls whether the player has reversed controls (1 = drunk, 0 = sober). */
    public int isDrunk = 1;

    /** Width of the game map in tiles. */
    private final float mapWidth;

    /** Height of the game map in tiles. */
    private final float mapHeight;

    /** Layer containing collision information from the tiled map. */
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
    public Player(Sprite sprite, float mapWidth, float mapHeight, TiledMapTileLayer collisionLayer) {
        super(sprite);

        //Initialize player animations
        initializeAnimations();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.collisionLayer =  collisionLayer;
    }

    /**
     * Loads all animation frames from the character spritesheet.
     * Sets up walking animations for all four directions plus standing poses.
     */
    private void initializeAnimations() {
        // Frame size: 25x49 pixels
        int frameWidth = 25;
        int frameHeight = 49;

        characterSheet = this.getTexture();

        // Standing poses (static, not animated)
        standLeft = new TextureRegion(characterSheet, 4, 10, frameWidth, frameHeight);
        standRight = new TextureRegion(characterSheet, 198, 9, frameWidth, frameHeight);
        standDown = new TextureRegion(characterSheet, 3, 65, frameWidth, frameHeight);
        standUp = new TextureRegion(characterSheet, 3, 120, frameWidth, frameHeight);

        // Walking LEFT (4 frames)
        TextureRegion[] walkLeftFrames = new TextureRegion[4];
        walkLeftFrames[0] = new TextureRegion(characterSheet, 44, 9, frameWidth, frameHeight);
        walkLeftFrames[1] = new TextureRegion(characterSheet, 78, 10, frameWidth, frameHeight);
        walkLeftFrames[2] = new TextureRegion(characterSheet, 117, 9, frameWidth, frameHeight);
        walkLeftFrames[3] = new TextureRegion(characterSheet, 151, 10, frameWidth, frameHeight);
        walkLeft = new Animation<>(0.1f, walkLeftFrames);

        // Walking RIGHT (4 frames)
        TextureRegion[] walkRightFrames = new TextureRegion[4];
        walkRightFrames[0] = new TextureRegion(characterSheet, 198, 64, frameWidth, frameHeight);
        walkRightFrames[1] = new TextureRegion(characterSheet, 230, 65, frameWidth, frameHeight);
        walkRightFrames[2] = new TextureRegion(characterSheet, 262, 65, frameWidth, frameHeight);
        walkRightFrames[3] = new TextureRegion(characterSheet, 292, 65, frameWidth, frameHeight);
        walkRight = new Animation<>(0.1f, walkRightFrames);

        // Walking FORWARD/DOWN (4 frames)
        TextureRegion[] walkDownFrames = new TextureRegion[4];
        walkDownFrames[0] = new TextureRegion(characterSheet, 43, 64, frameWidth, frameHeight);
        walkDownFrames[1] = new TextureRegion(characterSheet, 79, 66, frameWidth, frameHeight);
        walkDownFrames[2] = new TextureRegion(characterSheet, 115, 65, frameWidth, frameHeight);
        walkDownFrames[3] = new TextureRegion(characterSheet, 151, 66, frameWidth, frameHeight);
        walkDown = new Animation<>(0.1f, walkDownFrames);

        // Walking BACKWARDS/UP (4 frames)
        TextureRegion[] walkUpFrames = new TextureRegion[4];
        walkUpFrames[0] = new TextureRegion(characterSheet, 43, 121, frameWidth, frameHeight);
        walkUpFrames[1] = new TextureRegion(characterSheet, 78, 122, frameWidth, frameHeight);
        walkUpFrames[2] = new TextureRegion(characterSheet, 115, 121, frameWidth, frameHeight);
        walkUpFrames[3] = new TextureRegion(characterSheet, 152, 122, frameWidth, frameHeight);
        walkUp = new Animation<>(0.1f, walkUpFrames);

        // Set initial standing pose (facing down)
        currentStandingPose = standDown;
        stateTime = 0f;
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
        float speed = 5f;

        // If isDrunk == 1, controls are reversed
        if (isDrunk == 1) {
            // REVERSED CONTROLS
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                // RIGHT key -> move LEFT
                moveX = -speed * delta;
                currentAnimation = walkLeft;
                lastDirection = Direction.LEFT;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                // LEFT key -> move RIGHT
                moveX = speed * delta;
                currentAnimation = walkRight;
                lastDirection = Direction.RIGHT;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                // UP key -> move DOWN
                moveY = -speed * delta;
                currentAnimation = walkDown;
                lastDirection = Direction.DOWN;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                // DOWN key -> move UP
                moveY = speed * delta;
                currentAnimation = walkUp;
                lastDirection = Direction.UP;
                moving = true;
            }
        } else {
            // NORMAL CONTROLS
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                moveX = speed * delta;
                currentAnimation = walkRight;
                lastDirection = Direction.RIGHT;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                moveX = -speed * delta;
                currentAnimation = walkLeft;
                lastDirection = Direction.LEFT;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                moveY = speed * delta;
                currentAnimation = walkUp;
                lastDirection = Direction.UP;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                moveY = -speed * delta;
                currentAnimation = walkDown;
                lastDirection = Direction.DOWN;
                moving = true;
            }
        }

        if (moveX != 0 && canMoveTo(playerX + moveX, playerY)) {
            playerX += moveX;
        }
        if (moveY != 0 && canMoveTo(playerX, playerY + moveY)) {
            playerY += moveY;
        }

        if (!moving) {
            currentAnimation = null;
            switch (lastDirection) {
                case LEFT:
                    currentStandingPose = standLeft;
                    break;
                case RIGHT:
                    currentStandingPose = standRight;
                    break;
                case UP:
                    currentStandingPose = standUp;
                    break;
                case DOWN:
                default:
                    currentStandingPose = standDown;
                    break;
            }
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
    private boolean canMoveTo(float x, float y) {
        return isTileBlocked(x, y) &&
            isTileBlocked(x + playerSize, y) &&
            isTileBlocked(x, y + playerSize) &&
            isTileBlocked(x + playerSize, y + playerSize);
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

        if (tileId == 2 || tileId == 3 || tileId == 4 || tileId == 19 || tileId == 20 || tileId == 21 || tileId == 22) {
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
        if (currentAnimation != null) {
            // Walking - use animated frame
            frameToDraw = currentAnimation.getKeyFrame(stateTime, true);
        } else {
            // Standing - use static pose
            frameToDraw = currentStandingPose;
        }

        // Draw the character with proper aspect ratio
        // Frames are 25x49 pixels (width x height), aspect ratio = 49/25 = 1.96
        float aspectRatio = 49f / 25f;
        float drawHeight = playerSize * aspectRatio;
        batch.draw(frameToDraw, playerX, playerY, playerSize, drawHeight);
    }

}
