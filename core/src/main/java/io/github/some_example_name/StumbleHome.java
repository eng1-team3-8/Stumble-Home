package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Main game class that initializes core rendering resources and manages screen transitions.
 * Sets up the camera, viewport, and rendering batch used throughout the game.
 */
public class StumbleHome extends Game {

    /** Batch used for drawing sprites and textures. */
    public SpriteBatch batch;

    /** Font used for rendering text on screen. */
    public BitmapFont font;

    /** Camera for rendering the game world. */
    public OrthographicCamera camera;

    /** Viewport height in world units - zoomed for better visibility of player navigation. */
    final float VIEWPORT_HEIGHT = 12;

    /** Viewport managing screen-to-world coordinate mapping. */
    public Viewport viewport;


    /**
     * Called when the game is first created.
     * Initializes rendering resources, sets up the camera and viewport,
     * then transitions to the main menu screen.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        // aspect ratio = width/height.
        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();

        // create orthographic camera
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_HEIGHT*aspectRatio, VIEWPORT_HEIGHT, camera);
        viewport.apply();
        camera.position.set(viewport.getWorldWidth()/2, VIEWPORT_HEIGHT/2, 0);
        camera.update();

        this.setScreen(new MainMenuScreen(this));
    }

    /**
     * Renders the current screen.
     * Delegates to the active screen's render method.
     */
    public void render(){
        super.render();
    }

    /**
     * Cleans up resources when the game is closed.
     * Disposes of the sprite batch and font to prevent memory leaks.
     */
    public void dispose(){
        batch.dispose();
        if (font != null) {
            font.dispose();
        }
        this.screen.dispose();
    }
}
