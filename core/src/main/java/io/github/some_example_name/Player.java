package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;

public class Player extends Sprite {
    // Player animation
    public Texture characterSheet;
    private Animation<TextureRegion> walkDown;
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;
    private Animation<TextureRegion> walkUp;

    // Standing poses (static, not animated)
    private TextureRegion standDown;
    private TextureRegion standLeft;
    private TextureRegion standRight;
    private TextureRegion standUp;

    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> previousAnimation;
    private TextureRegion currentStandingPose;
    private float stateTime;

    // Track last direction for standing pose
    private enum Direction { DOWN, LEFT, RIGHT, UP }
    private Direction lastDirection = Direction.DOWN;

    // Player position and size
    public float playerX;
    public float playerY;
    public final float playerSize = 0.8f;

    private final float delta = Gdx.graphics.getDeltaTime();

    // Drunk state: 1 = drunk (reversed controls), 0 = sober (normal controls)
    public int isDrunk = 1;

    private final float mapWidth;
    private final float mapHeight;

    TiledMapTileLayer collisionLayer;

    public Player(Sprite sprite, float mapWidth, float mapHeight, TiledMapTileLayer collisionLayer) {
        super(sprite);

        //Initialize player animations
        initializeAnimations();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.collisionLayer =  collisionLayer;
    }

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

    public void input() {
        float moveX = 0;
        float moveY = 0;
        boolean moving = false;
        float speed = 5f;

        // If isDrunk == 1, controls are reversed
        if (isDrunk == 1) {
            // REVERSED CONTROLS
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                // RIGHT key -> move LEFT
                moveX = -speed * delta;
                currentAnimation = walkLeft;
                lastDirection = Direction.LEFT;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                // LEFT key -> move RIGHT
                moveX = speed * delta;
                currentAnimation = walkRight;
                lastDirection = Direction.RIGHT;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                // UP key -> move DOWN
                moveY = -speed * delta;
                currentAnimation = walkDown;
                lastDirection = Direction.DOWN;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                // DOWN key -> move UP
                moveY = speed * delta;
                currentAnimation = walkUp;
                lastDirection = Direction.UP;
                moving = true;
            }
        } else {
            // NORMAL CONTROLS
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveX = speed * delta;
                currentAnimation = walkRight;
                lastDirection = Direction.RIGHT;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveX = -speed * delta;
                currentAnimation = walkLeft;
                lastDirection = Direction.LEFT;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                moveY = speed * delta;
                currentAnimation = walkUp;
                lastDirection = Direction.UP;
                moving = true;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
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

    private void clampPlayerPosition() {
        playerX = MathUtils.clamp(playerX, 0, mapWidth - playerSize);
        playerY = MathUtils.clamp(playerY, 0, mapHeight - playerSize);
    }

    private boolean canMoveTo(float x, float y) {
        return isTileBlocked(x, y) &&
            isTileBlocked(x + playerSize, y) &&
            isTileBlocked(x, y + playerSize) &&
            isTileBlocked(x + playerSize, y + playerSize);
    }

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

    public void logic() {
        // Reset animation time if animation changed
        if (currentAnimation != previousAnimation) {
            stateTime = 0f;
            previousAnimation = currentAnimation;
        }

        // Update animation time
        stateTime += Gdx.graphics.getDeltaTime();
    }

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
