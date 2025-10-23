package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    // Declare the variables here
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite playerSprite;
    Texture playerTexture;
    Texture backgroundTexture;

    @Override
    public void create() {
        // Load the assets here
        // Create a simple 1x1 white pixel texture for now (placeholder until you add actual game assets)
        playerTexture = createPlaceholderTexture();
        backgroundTexture = createPlaceholderTexture();

        // Initialize the sprite based on the texture
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(1, 1);
        playerSprite.setPosition(3.5f, 2); // Center the player initially

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
    }

    /**
     * Creates a simple 1x1 white pixel texture as a placeholder.
     * Replace this with actual texture loading when you have game assets.
     */
    private Texture createPlaceholderTexture() {
        // Create a 1x1 white pixel texture programmatically
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        viewport.update(width, height, true);
    }

    @Override
    public void render() {
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
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // Clamp x and y to keep player within the viewport
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, worldWidth - playerSprite.getWidth()));
        playerSprite.setY(MathUtils.clamp(playerSprite.getY(), 0, worldHeight - playerSprite.getHeight()));
    }

    private void draw() {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        // Draw the background texture to fill the entire viewport
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        // Draw the player sprite
        playerSprite.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
        if (playerTexture != null) {
            playerTexture.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
