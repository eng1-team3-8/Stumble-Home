package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.stumblehome.StumbleHome;

/**
 * base abstract class for a screen containing menu content
 */
public abstract class MenuScreen implements Screen {
    protected StumbleHome game;

    // Background image for the main menu.
    protected Texture background;

    // Stage used for rendering UI elements such as buttons.
    protected Stage stage;

    // Skin used for styling UI components.
    protected Skin skin;

    /**
     * constructor for a menuScreen without a background
     * @param game that the screen is appearing on
     */
    public MenuScreen(final StumbleHome game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    }

    /**
     * constructor for a menu screen with a background
     * @param game that the screen is appearing on
     * @param background string that gives the path to the image to be used as the background
     */
    public MenuScreen(final StumbleHome game, String background) {
        this(game);
        this.background = new Texture(background);
    }
    /**
     * Called once per frame to render the menu screen.
     *
     * @param delta the time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.batch.setProjectionMatrix(
                game.camera
                        .projection
                        .cpy()
                        .setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        game.batch.begin();

        if (background != null) {
            game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    /** A menu screen should not by default need to change functionality when paused */
    @Override
    public void pause() {}

    @Override
    public void resume() {}

    /**
     * Called when the screen size changes (e.g., window resize).
     *
     * @param width the new width in pixels.
     * @param height the new height in pixels.
     */
    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
        // Resizes button hit zones (and more)
        this.show();
    }
}
