package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * The {@code MainMenuScreen} class is the main menu screen for the StumbleHome game.
 * <p>
 * It displays three interactive buttons: <b>Play</b>, <b>Tutorial</b>, and <b>Exit</b>.
 * The screen also supports showing a tutorial image overlay that can be closed by pressing
 * the ESC key or clicking the on screen.
 * </p>
 *
 * <p>This class implements LibGDX {@link Screen} interface, which shows
 * methods for managing a screen in a game.</p>
 *
 *
 */
public class MainMenuScreen implements Screen {

    final StumbleHome game;
    /** Background image for the main menu. */
    private Texture background;
    /** Image displayed when the tutorial is shown. */
    private Texture tutorialImage;
    /** Stage used for rendering UI elements such as buttons. */
    private Stage stage;
    /** Skin used for styling UI components. */
    private Skin skin;
    /** Flag that determines whether the tutorial image is currently displayed. */
    private boolean showTutorial = false;

    public MainMenuScreen(final StumbleHome game) {
        this.game = game;
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * <p>
     * Initializes the background, tutorial image, buttons, and input processing.
     * </p>
     */
    @Override
    public void show() {

        background = new Texture("MainMenu.png");
        tutorialImage = new Texture("tutorial.png");

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // create buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // set button positions
        float centerX = Gdx.graphics.getWidth() / 2f - 100;
        float startY = Gdx.graphics.getHeight() / 2f + 50;

        playButton.setBounds(centerX, startY, 200, 50);
        tutorialButton.setBounds(centerX, startY - 70, 200, 50);
        exitButton.setBounds(centerX, startY - 140, 200, 50);

        // adds buttons to stage
        stage.addActor(playButton);
        stage.addActor(tutorialButton);
        stage.addActor(exitButton);

        //  Play button click
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        // Tutorial button click
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showTutorial = true; // show popup when clicked
            }
        });

        // Exit button click
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // set stage to receive input
        Gdx.input.setInputProcessor(stage);
    }
    /**
     * Called every frame to render the screen.
     *
     * @param delta the time in seconds since the last render.
     */
    @Override
    public void render(float delta){
        ScreenUtils.clear(Color.BLACK);

        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        game.batch.begin();
        if (background != null) {
            game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        game.batch.end();

        if (stage != null) {
            stage.act(delta);
            stage.draw();
        }

        // draw tutorial popup if active
        if (showTutorial && tutorialImage != null) {
            game.batch.begin();
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            float imgWidth = tutorialImage.getWidth();
            float imgHeight = tutorialImage.getHeight();

            float scale = Math.min(screenWidth / imgWidth, screenHeight / imgHeight) * 0.8f; // 80% of screen
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
     * <p>Removes the input processor to prevent input handling when inactive.</p>
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
        // Resizes button hit zones (and more)
        this.show();
    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (tutorialImage != null) tutorialImage.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }
}
