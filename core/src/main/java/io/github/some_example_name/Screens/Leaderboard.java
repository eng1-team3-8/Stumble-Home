package io.github.some_example_name.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.some_example_name.StumbleHome;

public class Leaderboard extends MenuScreen{
    private ScrollPane scrollPane;

    public Leaderboard(StumbleHome game){
        this.game = game;
        // Draws leaderboard screen
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table leaderboard = new Table();

        for (int i = 0; i < 100; i++) {
            Label tempRow = new Label("leaderboard " + i, skin);
            tempRow.setFontScale(3f);
            leaderboard.add(tempRow).pad(10);
            leaderboard.row();
        }

        this.scrollPane = new ScrollPane(leaderboard, skin);
        scrollPane.setFillParent(true);
        stage.addActor(scrollPane);
    }

    @Override
    public void show() {
    }

    
    /**
     * Called once per frame to render the menu screen
     * Edited to capture input for scrollable table
     *
     * @param delta the time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        scrollPane.setScrollbarsVisible(true);
        ScreenUtils.clear(Color.BLACK);

        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        game.batch.begin();

        if (background != null) {
            game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        game.batch.end();
        stage.act(delta);
        stage.draw();

        // Exit button click
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            this.dispose();
            game.setScreen(new MainMenuScreen(game));
        }
    }


    /**
     * Called when the screen is resized.
     *
     * @param width  the new width of the screen in pixels.
     * @param height the new height of the screen in pixels.
     */
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'hide'");
    }

    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
        scrollPane.clear();
    }

}
