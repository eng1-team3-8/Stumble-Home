package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stumblehome.StumbleHome;
import java.util.Map;

public class Achievements extends MenuScreen {
    private ScrollPane scrollPane;
    private Map<String, Boolean> achievementData;
    private String[] achievements;
    private boolean playerAchievements;

    public Achievements(StumbleHome game, Map<String, Boolean> achievementData) {
        super(game);
        this.achievementData = achievementData;
        playerAchievements = true;
    }

    public Achievements(StumbleHome game) {
        super(game);
        playerAchievements = false;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Draws achievement screen screen
        Table leaderboard = achievementSetup();

        this.scrollPane = new ScrollPane(leaderboard, skin);
        scrollPane.setFillParent(true);
        stage.addActor(scrollPane);
    }

    /**
     * Called once per frame to render the menu screen Edited to capture input for scrollable table
     *
     * @param delta the time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        scrollPane.setScrollbarsVisible(true);
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

        // Escape button click
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            this.dispose();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'hide'");
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        scrollPane.clear();
    }

    public Table achievementSetup() {
        achievements =
                new String[] {
                    "Glad that wasn't Vodka!",
                    "Swipe the card!",
                    "Broken Ankle",
                    "Here's Johnny!",
                    "BEER ME!",
                    "Down the vodka",
                    "Lava Chicken... TASTY AS HELL",
                    "War of the Roses...",
                    "Traitor!!!",
                    "Switched sides have you??",
                    "Someone didn't like SYS1...",
                    "Raised from the dead... RUN!"
                };

        // Adds title to leaderboard
        Table leaderboard = new Table();
        Label tempRow = new Label("Achievements:", skin);
        tempRow.setFontScale(5f);
        leaderboard.add(tempRow).pad(15);
        leaderboard.row();

        if (playerAchievements) {
            for (String achievement : achievements) {
                if (achievementData.containsKey(achievement)) {
                    tempRow = new Label((achievement + ": Achieved"), skin);
                } else {
                    tempRow = new Label((achievement + ": X"), skin);
                }

                tempRow.setFontScale(3f);
                leaderboard.add(tempRow).pad(15);
                leaderboard.row();
            }
        } else {
            for (String achievement : achievements) {
                tempRow = new Label(achievement, skin);
                tempRow.setFontScale(3f);
                leaderboard.add(tempRow).pad(15);
                leaderboard.row();
            }
        }
        return leaderboard;
    }
}
