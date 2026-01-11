package io.github.stumblehome.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.stumblehome.StumbleHome;

/**
 * Black screen that is gone to for a set amount of time, used for player blackout event
 *
 * @author Isaac
 */
public class BlackoutScreen implements Screen {
    // time remaining for the screen to be shown
    private float time_remaining;
    // the game that the screen is on
    private StumbleHome game;
    // the game screen that the blackout screen should return to
    private GameScreen game_screen;

    /**
     * sets all the variables passed in to the relevant class variables
     */
    public BlackoutScreen(StumbleHome game, float time_remaining, GameScreen game_screen) {
        this.time_remaining = time_remaining;
        this.game = game;
        this.game_screen = game_screen;
    }

    @Override
    public void resize(int height, int width) {
        game_screen.resize(height, width);
    }

    /**
     * sets the screen to black, reduces the time remaining, if it hits 0 transitions back to game_screen
     */
    @Override
    public void render(float delta) {
        time_remaining -= delta;
        ScreenUtils.clear(Color.BLACK);

        if (time_remaining <= 0) {
            game.setScreen(game_screen);
        }
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

    @Override
    public void hide() {}
}
