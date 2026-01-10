package io.github.stumblehome.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.stumblehome.StumbleHome;

public class BlackoutScreen implements Screen {
    private float time_remaining;
    private StumbleHome game;
    private GameScreen game_screen;

    public BlackoutScreen(StumbleHome game, float time_remaining, GameScreen game_screen) {
        this.time_remaining = time_remaining;
        this.game = game;
        this.game_screen = game_screen;
    }

    @Override
    public void resize(int height, int width) {
        game_screen.resize(height, width);
    }

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
