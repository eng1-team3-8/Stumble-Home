package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final StumbleHome game;

    // Player animation
    private Texture characterSheet;
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
    private float playerX;
    private float playerY;
    private float playerSize = 0.8f;

    TiledMap map;
    OrthogonalTiledMapRenderer renderer;

    // Map boundaries
    private float mapWidth;
    private float mapHeight;
    private float minCameraX;
    private float maxCameraX;
    private float minCameraY;
    private float maxCameraY;

    public GameScreen(final StumbleHome game){
        this.game = game;


        //load the map, set unit scale to 1/16 (1 unit == 16 pixels)
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/16f);

        // Get map properties FIRST
        int mapWidthInTiles = map.getProperties().get("width", Integer.class);
        int mapHeightInTiles = map.getProperties().get("height", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = map.getProperties().get("tileheight", Integer.class);

        // Calculate map dimensions in world units (remember: 1 world unit = 16 pixels)
        mapWidth = mapWidthInTiles * tilePixelWidth / 16f;
        mapHeight = mapHeightInTiles * tilePixelHeight / 16f;

        // Calculate camera boundaries
        // The camera center can't get closer to the edge than half the viewport size
        float halfViewportWidth = game.viewport.getWorldWidth() / 2;
        float halfViewportHeight = game.viewport.getWorldHeight() / 2;

        minCameraX = halfViewportWidth;
        maxCameraX = mapWidth - halfViewportWidth;
        minCameraY = halfViewportHeight;
        maxCameraY = mapHeight - halfViewportHeight;

        // Debug: Print boundary information
        System.out.println("=== MAP BOUNDARIES DEBUG ===");
        System.out.println("Map size: " + mapWidth + " x " + mapHeight + " world units");
        System.out.println("Viewport size: " + game.viewport.getWorldWidth() + " x " + game.viewport.getWorldHeight() + " world units");
        System.out.println("Camera X range: " + minCameraX + " to " + maxCameraX);
        System.out.println("Camera Y range: " + minCameraY + " to " + maxCameraY);
        System.out.println("Camera starting position: " + game.camera.position.x + ", " + game.camera.position.y);
        System.out.println("===========================");

        // Initialize player animations
        initializeAnimations();

        // Position player at center of map
        playerX = mapWidth - 12  - playerSize / 2;
        playerY = mapHeight - 2 - playerSize / 2;

        // Center camera on player position
        game.camera.position.set(mapWidth / 2, mapHeight / 2, 0);
        System.out.println("Camera centered at: " + game.camera.position.x + ", " + game.camera.position.y);
        System.out.println("Player positioned at: " + playerX + ", " + playerY);
    }

    private void initializeAnimations() {
        // Load character sprite sheet
        characterSheet = new Texture("character.png");

        // Frame size: 25x49 pixels
        int frameWidth = 25;
        int frameHeight = 49;

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

        System.out.println("Animations initialized successfully!");
        System.out.println("Frame size: " + frameWidth + "x" + frameHeight);
    }

    @Override
    public void show() {
        // start playback of background music when screen is shown
        //music.play();
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 5f; // Player movement speed (units per second)
        float delta = Gdx.graphics.getDeltaTime();

        boolean moving = false;

        // Move PLAYER with arrow keys and set appropriate animation
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerX += speed * delta;
            currentAnimation = walkRight;
            lastDirection = Direction.RIGHT;
            moving = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerX -= speed * delta;
            currentAnimation = walkLeft;
            lastDirection = Direction.LEFT;
            moving = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerY += speed * delta;
            currentAnimation = walkUp;
            lastDirection = Direction.UP;
            moving = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerY -= speed * delta;
            currentAnimation = walkDown;
            lastDirection = Direction.DOWN;
            moving = true;
        }

        // If not moving, set standing pose based on last direction
        if (!moving) {
            currentAnimation = null;  // Not animating when standing
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

        // Clamp player position to stay within map boundaries
        clampPlayerPosition();
    }

    private void clampPlayerPosition() {
        // Player can't go beyond map edges (0 to mapWidth, 0 to mapHeight)
        // Account for player size
        playerX = MathUtils.clamp(
            playerX,
            0,  // Left edge
            mapWidth - playerSize  // Right edge (minus player width)
        );
        playerY = MathUtils.clamp(
            playerY,
            0,  // Bottom edge
            mapHeight - playerSize  // Top edge (minus player height)
        );
    }

    private void logic() {
        // Reset animation time if animation changed
        if (currentAnimation != previousAnimation) {
            stateTime = 0f;
            previousAnimation = currentAnimation;
        }

        // Update animation time
        stateTime += Gdx.graphics.getDeltaTime();

        // Make camera follow player
        // Camera should be centered on player (add half player size to get center)
        float playerCenterX = playerX + playerSize / 2;
        float playerCenterY = playerY + playerSize / 2;

        game.camera.position.set(playerCenterX, playerCenterY, 0);

        // Clamp camera to boundaries so we don't see beyond map edges
        clampCamera();
    }

    private void clampCamera() {
        // Only clamp if map is larger than viewport in each dimension
        if (mapWidth >= game.viewport.getWorldWidth()) {
            game.camera.position.x = MathUtils.clamp(
                game.camera.position.x,
                minCameraX,
                maxCameraX
            );
        }
        if (mapHeight >= game.viewport.getWorldHeight()) {
            game.camera.position.y = MathUtils.clamp(
                game.camera.position.y,
                minCameraY,
                maxCameraY
            );
        }
    }

    private void draw() {
        // Clear the screen with black color
        ScreenUtils.clear(Color.RED);

        game.camera.update();

        renderer.setView(game.camera);
        renderer.render();

        // Set batch to use camera's coordinate system
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();

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
        float drawWidth = playerSize;
        float drawHeight = playerSize * aspectRatio;
        game.batch.draw(frameToDraw, playerX, playerY, drawWidth, drawHeight);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        characterSheet.dispose();
    }
}
