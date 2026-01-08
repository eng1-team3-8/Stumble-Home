package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stumblehome.BossFight.BossFightLogic;
import io.github.stumblehome.BossFight.BossFightStatesManager;
import io.github.stumblehome.BossFight.Scissors;
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
    private final Texture MenuBackground;
    // Switch
    private final Texture Switch;
    // Cables
    private Texture BrokenCable;
    // Packets
    private Texture PacketUDP;

    // Music
    private Music BossMusic;

    // Sprite batch
    private SpriteBatch batch;
    private SpriteBatch background;

    // Stage
    private Stage optionsStage;
    private Stage infoStage;
    private Stage attackStage;
    private Stage finalAttackStage;
    private Stage winStage;

    // Text for info
    private BitmapFont font;

    // Viewport
    private FitViewport viewport;

    // Entities
    private Scissors scissors;
    private BossFightEntity cable1;
    private BossFightEntity cable2;
    private BossFightEntity cable3;
    private BossFightEntity cable4;

    // Game state manager
    private BossFightStatesManager statesFSA;

    // Logic handler
    private BossFightLogic logicHandler;

    // Health bars
    private NinePatch mikeHealth;
    private NinePatch playerHealth;
    private float mikeWidth;
    private float playerWidth;

    // The game screen
    private final GameScreen gameScreen;

    public BossScreen(
            final StumbleHome game, final String playerName, final GameScreen gameScreen) {
        this.game = game;
        this.playerName = playerName;
        this.gameScreen = gameScreen;

        this.batch = new SpriteBatch();
        this.background = new SpriteBatch();

        // Create the viewport
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.viewport.apply();

        // Create the state manager
        this.statesFSA = new BossFightStatesManager();

        // Creates the stage
        this.optionsStage = new Stage(viewport);
        this.infoStage = new Stage(viewport);
        this.attackStage = new Stage(viewport);
        this.finalAttackStage = new Stage(viewport);
        this.winStage = new Stage(viewport);

        // Creates the text
        this.font = new BitmapFont();
        this.font.setColor(Color.BLUE);

        // Create the BossFightEntities
        this.scissors =
                new Scissors(
                        new Texture("Sprites/BossFight/Scissors.png"), 100f, new float[] {105, 2});

        this.cable1 =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"), 150f, new float[] {199, 0});

        this.cable2 =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"), 150f, new float[] {287, 0});

        this.cable3 =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"), 150f, new float[] {375, 0});

        this.cable4 =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"), 150f, new float[] {463, 0});

        // Assign the textures
        this.MenuBackground = new Texture("Sprites/BossFight/Menu-Background.png");
        this.Switch = new Texture("Sprites/BossFight/Switch.png");
        this.BrokenCable = new Texture("Sprites/BossFight/Cable-Cut.png");
        this.PacketUDP = new Texture("Sprites/BossFight/UDP-Packet.png");

        // Music
        this.BossMusic = Gdx.audio.newMusic(Gdx.files.internal("Sprites/BossFight/Boss-Music.mp3"));
        this.BossMusic.setLooping(true);
        //        this.BossMusic.play(); // Uncomment when you want music

        // Create the logic handler
        this.logicHandler = new BossFightLogic(this.BrokenCable, this.statesFSA);

        // Health bars
        this.mikeHealth =
                new NinePatch(new Texture("Sprites/BossFight/RedGradient.png"), 0, 0, 0, 0);
        this.playerHealth =
                new NinePatch(new Texture("Sprites/BossFight/RedGradient.png"), 0, 0, 0, 0);
    }

    @Override
    public void show() {

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Options stage
        // Create Buttons
        TextButton attackButton = new TextButton("ATTACK", skin);
        TextButton infoButton = new TextButton("INFO", skin);

        // Set button positions
        float startX = viewport.getWorldWidth() / 5;
        float Y = viewport.getWorldHeight() / 5;

        attackButton.setBounds(startX, Y, 200, 50);
        infoButton.setBounds(startX * 3, Y, 200, 50);

        // Draw Buttons
        this.optionsStage.addActor(attackButton);
        this.optionsStage.addActor(infoButton);

        // Button listeners
        // Changes the state from options to attack
        attackButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (logicHandler.isFinalStage()) {
                            statesFSA.moveStates(0);
                        } else {
                            statesFSA.moveStates(1);
                        }
                    }
                });

        // Changes the state from options to info
        infoButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        statesFSA.moveStates(-1);
                    }
                });

        // Info stage
        // Create the back button
        TextButton backButton = new TextButton("BACK", skin);

        // Set button position and scale
        float X = (viewport.getWorldWidth() / 5) * 3;
        Y = viewport.getWorldHeight() / 5;

        backButton.setBounds(X, Y, 200, 50);

        // Draw the button
        this.infoStage.addActor(backButton);

        // Button listener
        backButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        statesFSA.moveStates(1);
                    }
                });

        // Create the final attack stage

        TextButton attackBossButton = new TextButton("ATTACK", skin);

        X = (viewport.getWorldWidth() / 2);
        Y = viewport.getWorldHeight() / 5;

        attackBossButton.setBounds(X, Y, 200, 50);

        // Draw the button
        this.finalAttackStage.addActor(attackBossButton);

        // Button listener
        attackBossButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        logicHandler.attackMike();
                        System.out.println(logicHandler.getMikeHealth() + " " + mikeWidth);
                        statesFSA.moveStates(-1);
                        logicHandler.mikeAttacks();
                    }
                });

        // Create the shared win/loss stage (there's only an acknowledgement button)

        TextButton leaveButton = new TextButton("LEAVE", skin);

        X = viewport.getWorldWidth() / 2;
        Y = viewport.getWorldHeight() / 2;

        leaveButton.setBounds(X, Y, 200, 50);

        // Draw the button
        this.winStage.addActor(leaveButton);

        // Button listener
        leaveButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //                    System.out.println(logicHandler.getMikeHealth() + " "
                        // + mikeWidth);
                        game.setScreen(gameScreen);
                    }
                });
    }

    @Override
    public void render(float delta) {
        // Make the Screen grey
        ScreenUtils.clear(Color.GRAY);

        this.mikeWidth = ((float) this.logicHandler.getMikeHealth() / 100) * 400;
        this.playerWidth = ((float) this.logicHandler.getPlayerHealth() / 100) * 100;

        // Draw the elements
        this.batch.begin();

        // Create Mike's health bar
        this.mikeHealth.draw(batch, 200, 400, mikeWidth, 50);
        this.mikeHealth.scale(50, 50);
        this.font.draw(batch, "MIKE FREEMAN", 360, 475);

        // Create the player's health bar
        this.playerHealth.draw(batch, 600, 10, playerWidth, 25);
        this.playerHealth.scale(20, 20);
        this.font.draw(batch, "Player Health", 610, 30);

        // This doesn't draw for some reason - it seemed it just decided from one day to the next
        // not to work
        this.batch.draw(
                this.MenuBackground, 1, 1, viewport.getWorldWidth(), viewport.getWorldHeight() / 2);

        switch (this.statesFSA.returnState()) {
            case OPTIONS:
                // Draw the stage and the buttons
                this.batch.end();
                this.optionsStage.draw();
                Gdx.input.setInputProcessor(this.optionsStage);
                break;
            case INFO:
                this.font.draw(batch, "Dr Mike J Freeman - the legendary lecturer", 100, 100);

                this.batch.end();

                this.infoStage.draw();
                Gdx.input.setInputProcessor(this.infoStage);

                break;
            case ATTACK:

                // Draw the switch
                this.batch.draw(
                        Switch,
                        viewport.getWorldWidth() / 4,
                        0,
                        viewport.getWorldWidth() / 2,
                        viewport.getWorldHeight() / 2);

                // Draw the cables
                this.logicHandler.drawCables(
                        new BossFightEntity[] {cable1, cable2, cable3, cable4}, batch);

                // Draw the scissors
                try {
                    this.logicHandler.moveScissors(
                            viewport,
                            batch,
                            scissors,
                            new BossFightEntity[] {cable1, cable2, cable3, cable4});
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                this.batch.end();

                Gdx.input.setInputProcessor(this.attackStage);

                break;

            case FINALATTACK:
                this.batch.end();
                this.finalAttackStage.draw();
                Gdx.input.setInputProcessor(this.finalAttackStage);
                break;
            case WIN:
                this.font.draw(
                        batch,
                        "You Win!!!!!!\r\nMike has retreated to some far off place (his office)",
                        100,
                        100);
                this.batch.end();
                this.winStage.draw();
                Gdx.input.setInputProcessor(this.winStage);
                break;
            case LOST:
                this.font.draw(
                        batch,
                        "You lost!\r\n"
                            + "Mike has gotten the better of you (he was going easy as well) and"
                            + " you now have plenty of time to reflect\r\n",
                        100,
                        100);
                this.batch.end();
                this.winStage.draw();
                Gdx.input.setInputProcessor(this.winStage);
        }

        //        this.batch.end();
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
