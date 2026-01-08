package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stumblehome.StumbleHome;

/**
 * The {@code MainMenuScreen} class is the main menu screen for the StumbleHome game.
 *
 * <p>It displays three interactive buttons: <b>Play</b>, <b>Tutorial</b>, and <b>Exit</b>. The
 * screen also supports showing a tutorial image overlay that can be closed by pressing the ESC key
 * or clicking the on screen.
 *
 * <p>This class implements LibGDX {@link Screen} interface, which shows methods for managing a
 * screen in a game.
 */
public class MainMenuScreen extends MenuScreen {
    TextField playerNameInput;
    // Image displayed when the tutorial is shown.
    private Texture tutorialImage;
    // Flag that determines whether the tutorial image is currently displayed.
    private boolean showTutorial = false;

    public MainMenuScreen(final StumbleHome game) {
        super(game, "MainMenu.png");
        // Textures for the main menu and the tutorial
        tutorialImage = new Texture("tutorial.png");
    }

    /**
     * Called when this screen becomes the current screen for the game.
     *
     * <p>Initializes the background, tutorial image, buttons, and input processing.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        // create buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);
        TextButton leaderBoardButton = new TextButton("Leaderboard", skin);
        TextButton achievementsButton = new TextButton("Achievements", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // set button positions
        float centerX = Gdx.graphics.getWidth() / 2f - 100;
        float startY = Gdx.graphics.getHeight() / 2f + 50;

        playerNameInput = new TextField("", skin);
        playerNameInput.setBounds(centerX, startY + 60, 200, 50);
        playerNameInput.setAlignment(1);
        playerNameInput.setMessageText("Enter Player Name");
        stage.addActor(playerNameInput);

        playButton.setBounds(centerX, startY, 200, 50);
        tutorialButton.setBounds(centerX, startY - 60, 200, 50);
        leaderBoardButton.setBounds(centerX - 105, startY - 120, 200, 50);
        achievementsButton.setBounds(centerX + 105, startY - 120, 200, 50);
        exitButton.setBounds(centerX, startY - 180, 200, 50);

        // adds buttons to stage
        stage.addActor(playButton);
        stage.addActor(tutorialButton);
        stage.addActor(leaderBoardButton);
        stage.addActor(achievementsButton);
        stage.addActor(exitButton);

        //  Play button click
        playButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (playerNameInput.getText().equals("")) {
                            // Display warning to enter username
                        } else {
                            game.setScreen(new GameScreen(game, playerNameInput.getText()));
                        }
                    }
                });

        // Tutorial button click
        tutorialButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        showTutorial = true; // show popup when clicked
                    }
                });

        // Tutorial button click
        leaderBoardButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new Leaderboard(game));
                    }
                });

        // Tutorial button click
        achievementsButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new Achievements(game));
                    }
                });

        // Exit button click
        exitButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.exit();
                    }
                });
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called every frame to render the screen.
     *
     * @param delta the time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        super.render(delta);

        // draw tutorial popup if active
        render_tutorial();
    }

    /** renders the tutorial page if it is not active, removes it if esc is pressed */
    private void render_tutorial() {
        if (showTutorial && tutorialImage != null) {
            game.batch.begin();
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            float imgWidth = tutorialImage.getWidth();
            float imgHeight = tutorialImage.getHeight();

            float scale = Math.min(screenWidth / imgWidth, screenHeight / imgHeight) * 0.8f;
            // 80% of screen

            float drawWidth = imgWidth * scale;
            float drawHeight = imgHeight * scale;

            // Center on screen
            float x = (screenWidth - drawWidth) / 2f;
            float y = (screenHeight - drawHeight) / 2f;

            game.batch.draw(tutorialImage, x, y, drawWidth, drawHeight);
            game.batch.end();
        }
        if (showTutorial) {
            // Close tutorial on ESC or click
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.justTouched()) {
                showTutorial = false; // close tutorial when pressing ESC or clicking
            }
        }
    }

    /**
     * Called when this screen is no longer the current screen for the game.
     *
     * <p>Removes the input processor to prevent input handling when inactive.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (tutorialImage != null) tutorialImage.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }
}
