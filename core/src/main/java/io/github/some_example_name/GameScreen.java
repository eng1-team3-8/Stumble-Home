package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final StumbleHome game;

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

    private Player player;

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

        //Initialize player
        player = new Player(new Sprite(new Texture("character.png")), mapWidth, mapHeight, collisionLayer);

        // Position player at start point of map
        player.playerX = mapWidth - 12  - player.playerSize / 2;
        player.playerY = mapHeight - 2 - player.playerSize / 2;

        // Center camera on player position
        game.camera.position.set(mapWidth / 2, mapHeight / 2, 0);
        System.out.println("Camera centered at: " + game.camera.position.x + ", " + game.camera.position.y);
        System.out.println("Player positioned at: " + player.playerX + ", " + player.playerY);
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
        player.input();
    }

    private void logic() {
        player.logic();

        // Make camera follow player
        // Camera should be centered on player (add half player size to get center)
        float playerCenterX = player.playerX + player.playerSize / 2;
        float playerCenterY = player.playerY + player.playerSize / 2;

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

        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        int minutes = (int)(remainingTime / 60);
        int seconds = (int)(remainingTime % 60);
        String timeText = String.format("%02d:%02d", minutes, seconds);

        game.batch.begin();

        player.draw(game.batch);
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
        player.characterSheet.dispose();
    }
}

