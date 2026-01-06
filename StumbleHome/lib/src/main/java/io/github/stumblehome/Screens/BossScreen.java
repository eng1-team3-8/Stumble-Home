package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.audio.Mp3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stumblehome.BossFight.BossFightStates;
import io.github.stumblehome.BossFight.BossFightStatesManager;
import io.github.stumblehome.Entities.BossFightEntity;
import io.github.stumblehome.StumbleHome;

public class BossScreen implements Screen {

    // The game instance
    private final StumbleHome game;

    // Players name
    private final String playerName;

    // Textures of the boss-fight object
    // Mike
    private Texture Mike;
    // Menu Background
    private Texture MenuBackground;
    // Switch
    private Texture Switch;
    // Cables
    private Texture BrokenCable;
    // Packets
    private Texture PacketUDP;

    // Music
    private Mp3.Music BossMusic;

    // Sprite batch
    private SpriteBatch batch;

    // Stage
    private Stage stage;

    // Viewport
    private FitViewport viewport;

    // Entities
    private BossFightEntity Scissors;
    private BossFightEntity Cable;

    // Game state manager
    private BossFightStatesManager statesFSA;

    public BossScreen(final StumbleHome game, final String playerName) {
        this.game = game;
        this.playerName = playerName;

        this.batch = new SpriteBatch();

        // Create the viewport
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.viewport.apply();

        // Create the state manager
        this.statesFSA = new BossFightStatesManager();

        // Create the BossFightEntities
        this.Scissors =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Scissors.png"), 1f, new float[] {2, 2});

        this.Cable =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"), 1f, new float[] {2, 3});

        // Assign the textures
        this.MenuBackground = new Texture("Sprites/BossFight/Menu-Background.png");
        this.Switch = new Texture("Sprites/BossFight/Switch.png");
        this.BrokenCable = new Texture("Sprites/BossFight/Cable-Cut.png");
        this.PacketUDP = new Texture("Sprites/BossFight/UDP-Packet.png");
    }

    @Override
    public void show() {
        if (this.statesFSA.returnState() == BossFightStates.OPTIONS) {
            this.stage = new Stage();

            Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

            // Create Buttons
            TextButton attackButton = new TextButton("ATTACK", skin);
            TextButton infoButton = new TextButton("INFO", skin);

            // Set button positions
            float startX = viewport.getWorldWidth() / 4;
            float Y = viewport.getWorldHeight() / 4;

            attackButton.setPosition(startX, Y);
            infoButton.setPosition(startX * 3, Y);

            // Set button scale
            attackButton.setTransform(true);
            infoButton.setTransform(true);

            attackButton.setScale(4f);
            infoButton.setScale(4f);

            // Draw Buttons
            this.stage.addActor(attackButton);
            this.stage.addActor(infoButton);
        }
    }

    @Override
    public void render(float delta) {
        // Make the Screen grey
        ScreenUtils.clear(Color.GRAY);

        // Only draw menu if we are in the select state
        if (this.statesFSA.returnState() == BossFightStates.OPTIONS) {
            // Draw the elements
            this.batch.begin();
            this.batch.draw(
                    MenuBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight() / 2);
            this.batch.end();

            // Draw the stage
            this.stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
