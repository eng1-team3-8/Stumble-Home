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
    Texture backgroundTexture;

    TiledMap map;
    OrthogonalTiledMapRenderer renderer;

    final float GAME_WORLD_WIDTH = 60;
    final float GAME_WORLD_HEIGHT = 30;

    public GameScreen(final StumbleHome game){
        this.game = game;

        //load the map, set unit scale to 1/16 (1 unit == 16 pixels)
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/16f);


        // load images for the player
        playerTexture = new Texture("bucket.png");

        // Initialize the player sprite
//        playerSprite = new Sprite(playerTexture);
//        playerSprite.setSize(1, 1);
//        playerSprite.setPosition(3.5f, 2); // Center the player initially
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
        float speed = 5f; // Units per second
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
    }

    private void logic() {
    }

    private void draw() {
        // Clear the screen with black color
        ScreenUtils.clear(Color.BLACK);

        game.camera.update();

        renderer.setView(game.camera);
        renderer.render();

        game.batch.begin();

        // Draw the player sprite
        //playerSprite.draw(game.batch);

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
