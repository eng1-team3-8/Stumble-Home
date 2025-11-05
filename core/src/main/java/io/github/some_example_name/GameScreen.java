package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
    private final float playerSize = 0.8f;

    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    TiledMapTileLayer collisionLayer;

    // Map boundaries
    private final float mapWidth;
    private final float mapHeight;
    private final float minCameraX;
    private final float maxCameraX;
    private final float minCameraY;
    private final float maxCameraY;

    private boolean paused = false;
    private float remainingTime = 20f;
    private boolean timeUp = false;





    public GameScreen(final StumbleHome game){
        this.game = game;


        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/16f);
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("hedge");

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
        // Toggle pause when SPACE is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            paused = !paused; // flip pause state
        }

        // Only run input and logic if not paused and there is enough time left
        if (!paused && !timeUp) {
            input();
            logic();
        }

        draw();

        // Draw pause overlay if paused
        if (paused) {
            drawPauseOverlay();
        }

        if (timeUp){
            drawTimeUpOverlay();
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

        private void input() {
        float speed = 5f;
        float delta = Gdx.graphics.getDeltaTime();

        float moveX = 0;
        float moveY = 0;
        boolean moving = false;

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

    private boolean isTileBlocked(float x, float y) {
        int tileX = (int) x;
        int tileY = (int) y;

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;

        int tileId = cell.getTile().getId();

        if (tileId == 5) return false;

        if (tileId == 2 || tileId == 3 || tileId == 4 || tileId == 19 || tileId == 20 || tileId == 21 || tileId == 22) {
            float yInTile = y - tileY;
            return yInTile < 0.4f;
        }

        return true;
    }

    private boolean canMoveTo(float x, float y) {
        return !isTileBlocked(x, y) &&
               !isTileBlocked(x + playerSize, y) &&
               !isTileBlocked(x, y + playerSize) &&
               !isTileBlocked(x + playerSize, y + playerSize);
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
        if (!paused) {
            if (remainingTime > 0) {
                remainingTime -= Gdx.graphics.getDeltaTime();
                if (remainingTime <= 0) {
                    remainingTime = 0;
                    timeUp = true; // trigger popup
                }
            }

        }


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

        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        int minutes = (int)(remainingTime / 60);
        int seconds = (int)(remainingTime % 60);
        String timeText = String.format("%02d:%02d", minutes, seconds);

        game.batch.begin();
        game.font.getData().setScale(4f);
        game.font.setColor(Color.WHITE);
        float marginX = 20;
        float marginY = Gdx.graphics.getHeight() - 20;
        game.font.draw(game.batch, timeText, marginX, marginY);
        game.font.getData().setScale(1f);
        game.batch.end();





    }
    private void drawPauseOverlay() {

        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        game.batch.begin();

        // Create a layout to measure text width & height
        GlyphLayout layout = new GlyphLayout();

        // Set font properties
        game.font.getData().setScale(5f);
        game.font.setColor(Color.WHITE);

        String pausedText = "PAUSED";

        // Calculate layout
        layout.setText(game.font, pausedText);

        // Compute centered position
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2f;

        // Draw centered text
        game.font.draw(game.batch, layout, x, y);

        // Reset scale
        game.font.getData().setScale(1f);

        game.batch.end();
    }
    private void drawTimeUpOverlay() {
        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );
        game.batch.begin();

        game.font.getData().setScale(6f);
        game.font.setColor(Color.RED);

        String message = "Game Over";

        GlyphLayout layout = new GlyphLayout(game.font, message);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2f;

        game.font.draw(game.batch, message, x, y);

        game.font.getData().setScale(1f);
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

