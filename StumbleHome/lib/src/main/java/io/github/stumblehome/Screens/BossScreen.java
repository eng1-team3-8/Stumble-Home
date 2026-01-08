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

/**
 * The class for the screen of the boss fight. This is where everything is drawn. The logic for
 * the bossfight is split between a few different classes:
 * The finite state machine is in BossFightStateManager, the logic is
 * in BossFightLogic, and the entities are in BossFightEntity and Scissors.
 *
 * @author Lenny
 */
public class BossScreen implements Screen {

    // The game instance
    private final StumbleHome game;

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
    private final SpriteBatch batch;

    // Stage
    private final Stage optionsStage;
    private final Stage infoStage;
    private final Stage attackStage;
    private final Stage finalAttackStage;
    private final Stage winStage;

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

    // Variable to move Mike
    private float mikeX;

    // The game screen
    private final GameScreen gameScreen;

  /**
   * This is the constructor for the bossfight.
   *
   * @param game StumbleHome: The game instance
   * @param gameScreen GameScreen: The gamescreen it has just come from, so it can return to it after the fight
   */
    public BossScreen(
            final StumbleHome game, final GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;

        this.batch = new SpriteBatch();

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
                        new Texture("Sprites/BossFight/Scissors.png"),
                        viewport.getWorldWidth() / 8,
                        new float[] {
                            (this.viewport.getWorldWidth() / 160) * 21,
                            this.viewport.getWorldHeight() / 250
                        });

        this.cable1 =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"),
                        (viewport.getWorldWidth() / 16) * 3,
                        new float[] {(this.viewport.getWorldWidth() / 800) * 199, 0});

        this.cable2 =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"),
                        (viewport.getWorldWidth() / 16) * 3,
                        new float[] {(this.viewport.getWorldWidth() / 800) * 287, 0});

        this.cable3 =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"),
                        (viewport.getWorldWidth() / 16) * 3,
                        new float[] {(this.viewport.getWorldWidth() / 32) * 15, 0});

        this.cable4 =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"),
                        (viewport.getWorldWidth() / 16) * 3,
                        new float[] {(this.viewport.getWorldWidth() / 800) * 463, 0});

        // Assign the textures
        this.Mike = new Texture("Sprites/BossFight/Mike.png");
        this.MenuBackground = new Texture("Sprites/BossFight/MenuBackground.png");
        this.Switch = new Texture("Sprites/BossFight/Switch.png");
        this.BrokenCable = new Texture("Sprites/BossFight/Cable-Cut.png");
        this.PacketUDP = new Texture("Sprites/BossFight/UDP-Packet.png");

        // Music
        this.BossMusic = Gdx.audio.newMusic(Gdx.files.internal("Sprites/BossFight/Boss-Music.mp3"));
        this.BossMusic.setLooping(true);
        this.BossMusic.play(); // Uncomment when you want music

        // Create the logic handler
        this.logicHandler = new BossFightLogic(this.BrokenCable, this.statesFSA, this.viewport);

        // Health bars
        this.mikeHealth =
                new NinePatch(new Texture("Sprites/BossFight/RedGradient.png"), 0, 0, 0, 0);
        this.playerHealth =
                new NinePatch(new Texture("Sprites/BossFight/RedGradient.png"), 0, 0, 0, 0);

        // Mike's X coordinate (for him to move at the end)
        this.mikeX = (viewport.getWorldWidth() / 2) - (this.viewport.getWorldWidth() / 80) * 9;
    }

  /**
   * This show method is where the various different stages are constructed.
   * The different stages correspond to the different states, with the only shared
   * stage being the win/loss stage.
   */
  @Override
    public void show() {

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        float buttonWidth = this.viewport.getWorldWidth() / 4;
        float buttonHeight = this.viewport.getWorldHeight() / 10;

        // Options stage
        // Create Buttons
        TextButton attackButton = new TextButton("ATTACK", skin);
        TextButton infoButton = new TextButton("INFO", skin);

        // Set button positions
        float startX = viewport.getWorldWidth() / 5;
        float Y = viewport.getWorldHeight() / 5;

        attackButton.setBounds(startX, Y, buttonWidth, buttonHeight);
        infoButton.setBounds(startX * 3, Y, buttonWidth, buttonHeight);

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

        backButton.setBounds(X, Y, buttonWidth, buttonHeight);

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

        attackBossButton.setBounds(X, Y, buttonWidth, buttonHeight);

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

        leaveButton.setBounds(X, Y, buttonWidth, buttonHeight);

        // Draw the button
        this.winStage.addActor(leaveButton);

        // Button listener
        leaveButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        BossMusic.stop();
                        game.setScreen(gameScreen);
                    }
                });
    }

  /**
   * This is where the different components get drawn depending on the
   * current state.
   * If there is any logic, external methods are called.
   *
   * @param delta The time in seconds since the last render.
   */
  @Override
    public void render(float delta) {
        // Make the Screen grey
        ScreenUtils.clear(Color.GRAY);

        System.out.println(viewport.getWorldWidth() + " " + viewport.getWorldHeight());

        this.mikeWidth = ((float) this.logicHandler.getMikeHealth() / 100) * 400;
        this.playerWidth = ((float) this.logicHandler.getPlayerHealth() / 100) * 100;

        // Draw the elements
        this.batch.begin();

        // If player won, Mike runs away
        if (this.logicHandler.checkIfWon()) {
            this.mikeX++;
        }

        // Draw Mike
        this.batch.draw(
                this.Mike,
                this.mikeX,
                viewport.getWorldHeight() / 2,
                (this.viewport.getWorldWidth() / 40) * 9,
                (this.viewport.getWorldHeight() / 25) * 9);

        // Draw the background for the menu
        this.batch.draw(
                this.MenuBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight() / 2);

        // Create Mike's health bar
        this.mikeHealth.draw(
                batch,
                this.viewport.getWorldWidth() / 4,
                (this.viewport.getWorldHeight() / 5) * 4,
                mikeWidth,
                this.viewport.getWorldHeight() / 10);
        this.mikeHealth.scale(
                this.viewport.getWorldWidth() / 16, this.viewport.getWorldWidth() / 16);
        this.font.draw(
                batch,
                "MIKE FREEMAN",
                (this.viewport.getWorldWidth() / 5) * 2,
                (this.viewport.getWorldHeight() / 20) * 19);

        // Create the player's health bar
        this.playerHealth.draw(
                batch,
                (this.viewport.getWorldWidth() / 4) * 3,
                this.viewport.getWorldHeight() / 50,
                playerWidth,
                25);
        this.playerHealth.scale(20, 20);
        this.font.draw(
                batch,
                "Player Health",
                (this.viewport.getWorldWidth() / 80) * 61,
                (this.viewport.getWorldHeight() / 50) * 3);

        switch (this.statesFSA.returnState()) {
            case OPTIONS:
                // Draw the stage and the buttons
                this.batch.end();
                this.optionsStage.draw();
                Gdx.input.setInputProcessor(this.optionsStage);
                break;
            case INFO:
                this.font.draw(
                        batch,
                        "Dr Mike J Freeman - the legendary lecturer",
                        this.viewport.getWorldWidth() / 8,
                        this.viewport.getWorldHeight() / 5);

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
                        this.viewport.getWorldWidth() / 8,
                        this.viewport.getWorldHeight() / 5);
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
                        this.viewport.getWorldWidth() / 8,
                        this.viewport.getWorldHeight() / 5);
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

  /**
   * Disposing all the components after the boss fight
   */
    @Override
    public void dispose() {
        // Dispose the entities first
        this.cable1.dispose();
        this.cable2.dispose();
        this.cable3.dispose();
        this.cable4.dispose();

        this.scissors.dispose();

        // Dispose the textures
        this.Mike.dispose();
        this.MenuBackground.dispose();
        this.Switch.dispose();
        this.BrokenCable.dispose();
        this.PacketUDP.dispose();

        // Dispose the spritebatch
        this.batch.dispose();

        // Dispose the stages
        this.infoStage.dispose();
        this.optionsStage.dispose();
        this.attackStage.dispose();
        this.finalAttackStage.dispose();
        this.winStage.dispose();

        // Dispose the music
        this.BossMusic.dispose();
    }
}
