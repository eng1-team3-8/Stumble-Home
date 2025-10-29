package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final StumbleHome game;


    Sprite playerSprite;
    Texture playerTexture;

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

        // Initialize the player sprite
        playerTexture = new Texture("bucket.png");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(game.viewport.getWorldWidth()/2,game.VIEWPORT_HEIGHT/2);

        // Get map properties
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

        // Center camera on map instead of starting at bottom-left corner
        game.camera.position.set(mapWidth / 2, mapHeight / 2, 0);
        System.out.println("Camera centered at: " + game.camera.position.x + ", " + game.camera.position.y);
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
        float speed = 10f; // Units per second
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //playerSprite.translateX(speed * delta);
            game.camera.translate(speed*delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //playerSprite.translateX(-speed * delta);
            game.camera.translate(-speed*delta,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
           // playerSprite.translateY(speed * delta);
            game.camera.translate(0,speed*delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //playerSprite.translateY(-speed * delta);
            game.camera.translate(0,-speed*delta);
        }

        // Clamp camera to boundaries after movement
        clampCamera();
    }

    private void logic() {
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

        game.batch.begin();

        // Draw the player sprite
        playerSprite.draw(game.batch);

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
        playerTexture.dispose();
    }
}
