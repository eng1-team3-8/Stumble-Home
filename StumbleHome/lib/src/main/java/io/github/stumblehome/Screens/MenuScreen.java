package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.stumblehome.StumbleHome;

public abstract class MenuScreen implements Screen {
    protected StumbleHome game;

    // Background image for the main menu.
    protected Texture background;

    // Stage used for rendering UI elements such as buttons.
    protected Stage stage;

    // Skin used for styling UI components.
    protected Skin skin;

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

    /**
     * A menu screen should not by default need to change functionality when paused
     */
    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
