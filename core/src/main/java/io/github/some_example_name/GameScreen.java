package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final StumbleHome game;

    Sprite playerSprite;
    Texture playerTexture;
    Texture backgroundTexture;

    public GameScreen(final StumbleHome game){
        this.game = game;

        // load images for the background + player
        backgroundTexture = new Texture("background.png");
        playerTexture = new Texture("bucket.png");

        // Initialize the player sprite
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(1, 1);
        playerSprite.setPosition(3.5f, 2); // Center the player initially
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
            playerSprite.translateX(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerSprite.translateX(-speed * delta); // Fixed: now moves left
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerSprite.translateY(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerSprite.translateY(-speed * delta);
        }
    }

    private void logic() {
        // Store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        // Clamp x and y to keep player within the viewport
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, worldWidth - playerSprite.getWidth()));
        playerSprite.setY(MathUtils.clamp(playerSprite.getY(), 0, worldHeight - playerSprite.getHeight()));
    }

    private void draw() {
        // Clear the screen with black color
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        // Draw the background texture to fill the entire viewport
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        // Draw the player sprite
        playerSprite.draw(game.batch);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        playerTexture.dispose();
    }
}
