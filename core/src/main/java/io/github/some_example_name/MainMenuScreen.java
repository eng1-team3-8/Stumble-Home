package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final StumbleHome game;

    public MainMenuScreen(final StumbleHome game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(Color.BLACK);


        if (Gdx.input.isTouched()) {
            game.setScreen((new GameScreen(game)));
            dispose();
        }
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
    }

    @Override
    public void dispose() {
    }
}
