package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stumblehome.StumbleHome;
import java.util.Map;

/**
 * The {@code WinScreen} class represents the screen displayed when the player successfully
 * completes the game and returns home.
 *
 * <p>It shows a congratulatory message, the player's remaining time, and score. It also includes a
 * "Restart" button that takes the player back to the main menu.
 *
 * <p>This class implements LibGDX {@link Screen} interface, shows methods for managing a screen in
 * a game.
 */
public class WinScreen extends GameFinishScreen {
    private Map<String, Integer> achievementData;

    /**
     * Constructs a new {@code WinScreen} instance.
     *
     * @param game the main {@link StumbleHome} game instance.
     * @param remainingTime the remaining time (in seconds) when the player won.
     * @param score the player's final score.
     */
    public WinScreen(
            StumbleHome game,
            float remainingTime,
            int score,
            Map<String, Integer> achievementData) {
        super(game, remainingTime, score, "MainMenu.png");
        this.achievementData = achievementData;
    }

    /**
     * Disposes of the screen and frees associated resources.
     *
     * <p>Called when the screen is no longer needed.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        // Create labels
        Label winLabel = new Label("You returned home!", skin);
        winLabel.setColor(Color.GREEN);
        winLabel.setFontScale(3f);
        winLabel.setAlignment(Align.center);

        Label timeLabel =
                new Label(String.format("Time Remaining: %.1f seconds", remainingTime), skin);
        Label scoreLabel = new Label("Score: " + score, skin);

        // Create restart button
        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                });

        // Create leaderboard button
        TextButton leaderBoardButton = new TextButton("Leaderboard", skin);
        leaderBoardButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new Leaderboard(game));
                    }
                });

        // Create achievements button
        TextButton achievementsButton = new TextButton("Achievements", skin);
        achievementsButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new Achievements(game, achievementData));
                    }
                });

        // Layout using a table
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        float buttonWidth = Gdx.graphics.getWidth() * 0.25f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.1f;

        table.add(winLabel).padBottom(40).row();
        table.add(timeLabel).padBottom(20).row();
        table.add(scoreLabel).padBottom(40).row();
        table.add(restartButton).padBottom(40).width(buttonWidth).height(buttonHeight).row();
        table.add(leaderBoardButton).padBottom(40).width(buttonWidth).height(buttonHeight).row();
        table.add(achievementsButton).padBottom(40).width(buttonWidth).height(buttonHeight).row();

        stage.addActor(table);
    }

    @Override
    public void hide() {}
}
