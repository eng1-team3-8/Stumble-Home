package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stumblehome.StumbleHome;

/**
 * The {@code GameOverScreen} class represents the screen displayed when the player does not
 * complete the game and returns home.
 *
 * <p>It shows a message, the player's remaining time, and score. It also includes a "Restart"
 * button that takes the player back to the main menu.
 *
 * <p>This class implements LibGDX {@link Screen} interface, shows methods for managing a screen in
 * a game.
 */
public class LoseScreen extends GameFinishScreen {
    /**
     * Constructs a new {@code GameOverScreen} instance.
     *
     * @param game the main {@link StumbleHome} game instance.
     * @param remainingTime the remaining time (in seconds) when the player lost.
     * @param score the player's final score.
     */
    public LoseScreen(StumbleHome game, float remainingTime, int score) {
        super(game, remainingTime, score, "MainMenu.png");
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

    /**
     * draws the layout of the lose screen, including a restart button
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
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

        // Layout using a table
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        float buttonWidth = Gdx.graphics.getWidth() * 0.25f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.1f;

        table.add(timeLabel).padBottom(10).row();
        table.add(scoreLabel).padBottom(10).row();
        table.add(restartButton).width(buttonWidth).height(buttonHeight);

        stage.addActor(table);
    }

    @Override
    public void hide() {}
}
