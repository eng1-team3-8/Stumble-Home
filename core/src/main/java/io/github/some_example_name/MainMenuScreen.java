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

public class MainMenuScreen implements Screen {
    final StumbleHome game;
    private Texture background;
    private Texture tutorialImage;
    private Stage stage;
    private Skin skin;
    private boolean showTutorial = false;

    public MainMenuScreen(final StumbleHome game) {
        this.game = game;
    }

    @Override
    public void show() {

        background = new Texture("background.jpg");
        tutorialImage = new Texture("tutorialImage.jpg");

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Added: create buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Added: set button positions
        float centerX = Gdx.graphics.getWidth() / 2f - 100;
        float startY = Gdx.graphics.getHeight() / 2f + 50;

        playButton.setBounds(centerX, startY, 200, 50);
        tutorialButton.setBounds(centerX, startY - 70, 200, 50);
        exitButton.setBounds(centerX, startY - 140, 200, 50);

        // Added: add buttons to stage
        stage.addActor(playButton);
        stage.addActor(tutorialButton);
        stage.addActor(exitButton);

        //  Added: handle Play button click
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        // Added: handle Tutorial button click
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showTutorial = true; // show popup when clicked
            }
        });

        // Added: handle Exit button click
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Added: set stage to receive input
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(Color.BLUE);

        game.batch.begin();
        if (background != null) {
            game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        game.font.draw(game.batch, "Title Screen!", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight()* .75f);


        game.batch.end();

        if (stage != null) {
            stage.act(delta);
            stage.draw();


        }

        // Added: draw tutorial popup if active
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
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.justTouched()) {
                showTutorial = false; // close tutorial when pressing ESC or clicking
            }
        }



    }

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
    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (tutorialImage != null) tutorialImage.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }
}
