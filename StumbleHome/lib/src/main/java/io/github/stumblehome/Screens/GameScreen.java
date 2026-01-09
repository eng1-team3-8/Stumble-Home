package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.audio.Mp3.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stumblehome.Entities.Alcohol;
import io.github.stumblehome.Entities.BirdSeed;
import io.github.stumblehome.Entities.Bob;
import io.github.stumblehome.Entities.BottleEvent;
import io.github.stumblehome.Entities.Chainsaw;
import io.github.stumblehome.Entities.Food;
import io.github.stumblehome.Entities.KeycardEvent;
import io.github.stumblehome.Entities.LongBoiEvent;
import io.github.stumblehome.Entities.Player;
import io.github.stumblehome.Entities.Rose;
import io.github.stumblehome.Entities.Twig;
import io.github.stumblehome.Messages.MessageHandler;
import io.github.stumblehome.Messages.Messages;
import io.github.stumblehome.StumbleHome;
import java.util.HashMap;

/**
 * The {@code GameScreen} class represents the main gameplay screen in the StumbleHome game.
 *
 * <p>It handles player movement, camera control, event interactions, time tracking, and determines
 * win or loss conditions.
 *
 * <p>The class uses LibGDX's {@link Screen} interface to define game lifecycle behavior such as
 * rendering, resizing, and disposal.
 *
 * <p>In this screen:
 *
 * <ul>
 *   <li>The player navigates through a tiled map to reach the finish zone.
 *   <li>Various events (e.g., bottle, keycard, Long Boi) influence the gameplay.
 *   <li>A timer counts down, ending the game when it reaches zero.
 * </ul>
 */
public class GameScreen implements Screen {
    // Reference to the main game instance.
    final StumbleHome game;
    // Map boundaries
    // Width of the map in world units.
    private final float mapWidth;
    // Height of the map in world units.
    private final float mapHeight;
    // Minimum X coordinate for the camera position.
    private final float minCameraX;
    // Maximum X coordinate for the camera position.
    private final float maxCameraX;
    // Minimum Y coordinate for the camera position.
    private final float minCameraY;
    // Maximum Y coordinate for the camera position.
    private final float maxCameraY;
    // The player character instance.
    private final Player player;
    // Interactive event: the water bottle (removes drunkenness).
    private final BottleEvent bottle;
    // Interactive event: Long Boi (a moving hazard).
    private final LongBoiEvent longBoi;
    // Interactive event: keycard (required to win).
    private final KeycardEvent keycard;
    // Interactive event: Twig (slows down walking)
    private final Twig twig;
    // Beer for the negative event
    private final Alcohol beer;
    // Vodka for the negative event
    private final Alcohol vodka;
    ;
    // Chicken for the positive event
    private final Food chicken;
    // Adding the Yorks and the Lancs roses
    private final Rose York;
    private final Rose Lancaster;
    // Chainsaw for the positive event
    private final Chainsaw chainsaw;
    // Bob
    private final Bob bob;
    // BirdSeed
    private final BirdSeed birdSeed;
    private final String playerName;
    // The current map being rendered.
    TiledMap map;
    // Renders the tiled map using an orthogonal projection.
    OrthogonalTiledMapRenderer renderer;
    // Collision layer representing obstacles (e.g., hedges).
    TiledMapTileLayer collisionLayer;
    // Indicates whether the game is currently paused.
    private boolean paused = false;
    // Indicate when a blackout is occuring
    private boolean blackout = false;
    // Remaining time for the player to complete the game (in seconds).
    private float remainingTime = 300f;
    // Whether the countdown timer has reached zero.
    private boolean timeUp = false;
    // Whether the player has reached the finish zone.
    private boolean reachedFinish = false;
    // Whether the player has collected the keycard.
    private boolean hasKeycard = false;
    // Message timers for temporary on-screen notifications.
    private MessageHandler msg;
    // Counters for hidden, helpful, and hindering events.
    private int hiddenEventCounter = 0;
    private int helpfulEventCounter = 0;
    private int hinderingEventCounter = 0;

    // Achievements Popup
    private Stage stage;
    private Dialog achievementBox;
    private boolean eventTriggered = false;
    private float oldWidth = 0;
    private float dialogScaleFactor = 0;

    // Achievement Tracker
    HashMap<String, Boolean> achievementData = new HashMap<>();

    // Music setting tracker
    final boolean musicToggle;
    Music music;
    float volume;

    /**
     * Constructs the {@code GameScreen} and initializes the map, player, camera, and in-game events.
     *
     * @param game the main {@link StumbleHome} game instance.
     */
    public GameScreen(
            final StumbleHome game,
            final String playerName,
            final boolean musicToggle,
            float volume) {
        this.game = game;
        this.playerName = playerName;
        this.musicToggle = musicToggle;
        this.volume = volume;

        map = new TmxMapLoader().load("map2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("hedge");

        // Get map properties first
        int mapWidthInTiles = map.getProperties().get("width", Integer.class);
        int mapHeightInTiles = map.getProperties().get("height", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = map.getProperties().get("tileheight", Integer.class);

        // Calculate map dimensions in world units (remember: 1 world unit = 16 pixels)
        mapWidth = mapWidthInTiles * tilePixelWidth / 16f;
        mapHeight = mapHeightInTiles * tilePixelHeight / 16f;

        // Calculate camera boundaries
        // The camera center can't get closer to the edge than half the viewport size
        float halfViewportWidth = game.viewport.getWorldWidth() / 2;
        float halfViewportHeight = game.viewport.getWorldHeight() / 2;

        minCameraX = halfViewportWidth;
        maxCameraX = mapWidth - halfViewportWidth;
        minCameraY = halfViewportHeight;
        maxCameraY = mapHeight - halfViewportHeight;

        // Initialize player
        player =
                new Player(
                        new Texture(Player.ASSET),
                        0.8f,
                        new float[] {60f, 50f},
                        mapWidth,
                        mapHeight,
                        collisionLayer);

        // Center camera on player position
        game.camera.position.set(mapWidth / 2, mapHeight / 2, 0);

        // events
        // Bottle event (not reimplemented yet)
        bottle = new BottleEvent(new Texture(BottleEvent.ASSET), 1f, new float[] {67f, 29f});

        // LongBoi event
        longBoi =
                new LongBoiEvent(
                        new Texture(LongBoiEvent.ASSET),
                        2f,
                        new float[] {48f, 13f},
                        collisionLayer);

        // Keycard event (not reimplemented yet)
        keycard = new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {15f, 46f});

        // Twig event
        twig = new Twig(new Texture(Twig.ASSET), 1f, new float[] {51f, 19f});

        // Alcohol bottles
        beer = new Alcohol(new Texture(Alcohol.ASSET_TSING), 1.5f, new float[] {62f, 5f});
        vodka = new Alcohol(new Texture(Alcohol.ASSET_SMIRN), 1f, new float[] {35f, 2f});

        // Chicken
        chicken = new Food(new Texture(Food.ASSET), 1f, new float[] {46.5f, 33f});

        // The roses
        York = new Rose(new Texture(Rose.ASSET_YORK), 1f, new float[] {2f, 25f});
        Lancaster = new Rose(new Texture(Rose.ASSET_LANC), 1f, new float[] {5f, 25f});

        // Chainsaw
        chainsaw = new Chainsaw(new Texture(Chainsaw.ASSET), 1f, new float[] {50f, 13f});

        // Bob
        bob = new Bob(new Texture(Bob.ASSET), 1f, new float[] {17f, 20f});

        // Bird seed
        birdSeed = new BirdSeed(new Texture(BirdSeed.ASSET), 1f, new float[] {48f, 20f});

        // message handler
        msg = new MessageHandler(game);
        msg.addMessage(Messages.PAUSED, "PAUSED", Color.WHITE, 2f);
        msg.addMessage(
                Messages.NOKEYCARD, "You need a KeyCard to enter the Door...", Color.RED, 2f);
        msg.addMessage(
                Messages.PICKUPKEYCARD,
                "You have the keyCard, you can go home now!",
                Color.YELLOW,
                2f);
        msg.addMessage(
                Messages.REMEMBERKEYCARD,
                "You remembered that you don't have the keycard, find it!",
                Color.RED,
                2f);
        msg.addMessage(Messages.LONGBOIAPPEAR, "It's Long Boi! Avoid him!", Color.RED, 2f);
        msg.addMessage(
                Messages.YORKSQUISHED,
                "Blud does NOT like York, I bet YOU killed Longboi!",
                Color.RED,
                2f);
        msg.addMessage(
                Messages.LANCASTERSQUISHED,
                "Good job, doing God's work here. Goodbye Lancashire!",
                Color.RED,
                2f);
        msg.addMessage(Messages.BOTHSQUISHED, "I see, you just harbour chaos", Color.BLACK, 2f);
        msg.addMessage(
                Messages.CHAINSAWPICKEDUP,
                "You've obtained a chainsaw. press e to cut a line of hedges! (Single use)",
                Color.RED,
                2f);
        msg.addMessage(
                Messages.BIRDSEED,
                "LongBoi is really hungry, look how fast he is!",
                Color.BLACK,
                2f);

        // Achievements Popup
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        achievementBox = new Dialog("Achievement", skin);
        achievementBox.getTitleTable().padBottom(15f);
        achievementBox.setColor(Color.LIME);
        achievementBox.scaleBy(0.25f);

        oldWidth = Gdx.graphics.getWidth();

        // Plays music if enabled
        if (musicToggle) {
            music = (Music) Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
            music.setLooping(true);
            music.setVolume(volume);
            music.play();
        }
    }

    @Override
    public void show() {}

    /**
     * Called once per frame to update and render the game state.
     *
     * <p>Handles player input, game logic, event interactions, and draws all entities and UI
     * elements.
     *
     * @param delta the time (in seconds) since the last render.
     */
    @Override
    public void render(float delta) {
        // Toggle pause when SPACE is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused; // flip pause state

            toggleMusic();
        }

        if (eventTriggered) {
            achievementBox.show(stage);
            eventTriggered = false;

            achievementBox.addAction(
                    Actions.sequence(
                            Actions.delay(5f),
                            Actions.fadeOut(0.5f),
                            Actions.run(() -> achievementBox.hide())));
        }

        // Only run player input and logic if not paused and there is enough time left
        if (!paused && !timeUp && !blackout) {
            player.input();
            logic();
        }
        if (blackout) {
            this.remainingTime -= 60;
            hinderingEventCounter++;
            blackout = false;
            game.setScreen(new BlackoutScreen(this.game, 3f, this));
        }
        draw();

        msg.updateMessages();
        msg.updateMessage(paused, Messages.PAUSED);

        if (timeUp && !reachedFinish) {
            int score = (hiddenEventCounter + helpfulEventCounter + hinderingEventCounter) * 50;

            if (musicToggle) {
                music.stop();
            }

            game.setScreen(new LoseScreen(game, remainingTime, score));
        }

        positionDialogueBox();

        // Applies correct viewport for screen size
        stage.getViewport().apply();
        // Draws the achievements box on the stage
        stage.act(delta);
        stage.draw();
    }

    /**
     * Handles core game logic, including player movement, event interactions, collision detection,
     * and time management.
     */
    private void logic() {
        player.logic();

        // events logic
        bottle.logic();
        keycard.logic();
        longBoi.logic();

        float playerCentreX = player.getX() + player.frame_size / 2;
        float playerCentreY = player.getY() + player.frame_size / 2;

        // Check collision between player and water bottle
        if (bottle.checkColliding(playerCentreX, playerCentreY)) {
            // Player collected the water bottle - becomes sober
            if (player.isPlayerDrunk()) {
                msg.set_time(Messages.REMEMBERKEYCARD, 3f);
            }
            player.setSober();
            helpfulEventCounter++;

            setAchievementText("Glad that wasn't Vodka!");
        }

        // check collision with keycard
        if (keycard.checkColliding(playerCentreX, playerCentreY)) {
            hasKeycard = true;
            hinderingEventCounter++;
            msg.set_time(Messages.PICKUPKEYCARD, 3f);

            setAchievementText("Swipe the card!");
        }

        // Check collision with twig
        if (twig.checkColliding(playerCentreX, playerCentreY)) {
            hinderingEventCounter++;
            player.slowDownPlayer(1f);

            setAchievementText("Broken Ankle");
        }

        if (chainsaw.checkColliding(playerCentreX, playerCentreY)) {
            helpfulEventCounter++;
            msg.set_time(Messages.CHAINSAWPICKEDUP, 3f);
            setAchievementText("Here's Johnny!");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            // player attempts to use chainsaw
            chainsaw.UseChainsaw(player, collisionLayer);
        }
        // System.out.println(player.isPlayerDrunk());
        // Check if collision with beer
        if (beer.checkColliding(playerCentreX, playerCentreY)) {
            if (!achievementData.containsKey("Down the vodka")) {
                hinderingEventCounter++;
            }
            if (vodka.is_collected && player.isPlayerDrunk()) {
                blackout = true;
            }
            beer.makeDrunk(player);

            setAchievementText("BEER ME!");
        }

        // Check if collision with vodka
        if (vodka.checkColliding(playerCentreX, playerCentreY)) {
            if (!achievementData.containsKey("BEER ME!")) {
                hinderingEventCounter++;
            }
            if (beer.is_collected && player.isPlayerDrunk()) {
                blackout = true;
            }
            vodka.makeDrunk(player);

            setAchievementText("Down the vodka");
        }

        // Check if collision with chicken
        if (chicken.checkColliding(playerCentreX, playerCentreY)) {
            helpfulEventCounter++;
            if (player.isPlayerDrunk()) {
                msg.set_time(Messages.REMEMBERKEYCARD, 3f);
            }
            chicken.eatFood(player);

            setAchievementText("Lava Chicken... TASTY AS HELL");
        }

        // Check if the Yorks rose has been stepped on
        if (York.checkColliding(playerCentreX, playerCentreY)) {
            if (!Lancaster.isSquished) {
                hiddenEventCounter++;
            }
            msg.set_time(Messages.YORKSQUISHED, 3f);
            York.isSquished = true;

            setAchievementText("Traitor!!!");
        }

        // Check if the Lancaster rose has been stepped on
        if (Lancaster.checkColliding(playerCentreX, playerCentreY)) {
            if (!York.isSquished) {
                hiddenEventCounter++;
            }
            msg.set_time(Messages.LANCASTERSQUISHED, 3f);
            Lancaster.isSquished = true;

            setAchievementText("War of the Roses...");
        }

        // CHeck if both roses have been squished.
        // If true send a message and then immediately set squished to false, to prevent it
        // repeating
        if (York.isSquished && Lancaster.isSquished) {
            msg.set_time(Messages.BOTHSQUISHED, 3f);
            York.isSquished = false;
            Lancaster.isSquished = false;

            setAchievementText("Switched sides have you??");
        }

        // Check if Bob has been squished
        if (bob.checkColliding(playerCentreX, playerCentreY)) {
            hiddenEventCounter++;

            toggleMusic();
            game.setScreen(new BossScreen(game, this, musicToggle, volume));

            setAchievementText("Someone didn't like SYS1...");
        }

        // Check if birdSeed has been collided with
        if (birdSeed.checkColliding(playerCentreX, playerCentreY)) {
            hinderingEventCounter++;
            msg.set_time(Messages.BIRDSEED, 3f);

            longBoi.setSpeed(5f);

            setAchievementText("Feed the Bird");
        }

        // if long boi walk not completed, run logic
        if (!longBoi.done_walk) {
            // check if player near longBoi
            if (longBoi.checkNear(playerCentreX, playerCentreY)) {
                // player is near
                msg.set_time(Messages.LONGBOIAPPEAR, 2f);
                hiddenEventCounter++;

                setAchievementText("Raised from the dead... RUN!");
            }

            // walk long boi as long as 'near' variable set to true
            if (longBoi.getNear()) {
                longBoi.walkPath();
            }

            // check if player collided with longBoi
            if (longBoi.checkColliding(playerCentreX, playerCentreY)) {
                int score = (hiddenEventCounter + helpfulEventCounter + hinderingEventCounter) * 50;

                if (musicToggle) {
                    music.stop();
                }

                game.setScreen(new LoseScreen(game, remainingTime, score));
            }
        }

        // Make camera follow player
        // Camera should be centered on player (add half player size to get center)
        float playerCenterX = player.getX() + player.frame_size / 2;
        float playerCenterY = player.getY() + player.frame_size / 2;

        game.camera.position.set(playerCenterX, playerCenterY, 0);

        // logic for time running out
        if (!paused && !reachedFinish) {
            if (remainingTime > 0) {
                // decrease time
                remainingTime -= Gdx.graphics.getDeltaTime();
                if (remainingTime <= 0) {
                    remainingTime = 0;
                    timeUp = true;
                }
            }
        }

        // if reached finish successfully (has keycard)
        if (!reachedFinish && hasKeycard && reachedFinishZone()) {
            timeUp = true;
            paused = true;

            int score =
                    (int) (remainingTime * 10)
                            + (hiddenEventCounter + helpfulEventCounter + hinderingEventCounter)
                                    * 50;
            saveLeaderBoardScore(score);

            reachedFinish = true;
            game.setScreen(new WinScreen(game, remainingTime, score, achievementData));
            dispose();
        }

        // if at finish with no keycard, show no keycard message
        if (!hasKeycard && reachedFinishZone()) {
            msg.set_time(Messages.NOKEYCARD, 3f);
        }

        clampCamera();
    }

    private boolean reachedFinishZone() {
        float finishZoneX = 2f;
        float finishZoneY = 6f;
        float finishZoneWidth = 5f;
        float finishZoneHeight = 3f;
        return player.getX() < finishZoneX + finishZoneWidth
                && player.getX() + player.frame_size > finishZoneX
                && player.getY() < finishZoneY + finishZoneHeight
                && player.getY() + player.frame_size > finishZoneY;
    }

    private void clampCamera() {
        // Only clamp if map is larger than viewport in each dimension
        if (mapWidth >= game.viewport.getWorldWidth()) {
            game.camera.position.x =
                    MathUtils.clamp(game.camera.position.x, minCameraX, maxCameraX);
        }
        if (mapHeight >= game.viewport.getWorldHeight()) {
            game.camera.position.y =
                    MathUtils.clamp(game.camera.position.y, minCameraY, maxCameraY);
        }
    }

    /** Draws all visible elements: map, player, events, and UI text. */
    private void draw() {
        game.viewport.apply();
        // Clear the screen with black color
        ScreenUtils.clear(Color.BLACK);

        game.camera.update();

        renderer.setView(game.camera);
        renderer.render();

        // Set batch to use camera's coordinate system
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();

        // draw player
        player.draw(game.batch);

        // draw events
        bottle.draw(game.batch);
        longBoi.draw(game.batch);
        keycard.draw(game.batch);
        twig.draw(game.batch);
        beer.draw(game.batch);
        vodka.draw(game.batch);
        chicken.draw(game.batch);
        York.draw(game.batch);
        Lancaster.draw(game.batch);
        chainsaw.draw(game.batch);
        bob.draw(game.batch);
        birdSeed.draw(game.batch);

        game.batch.end();

        // UI overlay (timer and event counters)
        game.batch.setProjectionMatrix(
                game.camera
                        .projection
                        .cpy()
                        .setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        int minutes = (int) (remainingTime / 60);
        int seconds = (int) (remainingTime % 60);
        String timeText = String.format("%02d:%02d", minutes, seconds);

        String helpfulEventText = "Helpful events: " + helpfulEventCounter + "/" + 3;
        String hinderingEventText = "Hindering events: " + hinderingEventCounter + "/" + 5;
        String hiddenEventText = "Hidden events: " + hiddenEventCounter + "/" + 3;

        game.batch.begin();

        game.font.getData().setScale(3f);
        game.font.setColor(Color.WHITE);
        float marginX = 20;
        float marginY = Gdx.graphics.getHeight() - 20;
        // draw text for time
        game.font.draw(game.batch, timeText, marginX, marginY);
        // draw text for events
        game.font.getData().setScale(1.5f);
        game.font.draw(game.batch, helpfulEventText, marginX, 30);
        game.font.draw(game.batch, hinderingEventText, marginX, 60);
        game.font.draw(game.batch, hiddenEventText, marginX, 90);

        game.batch.end();
    }

    /**
     * Called when the screen size changes (e.g., window resize).
     *
     * @param width the new width in pixels.
     * @param height the new height in pixels.
     */
    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
        stage.getViewport().update(width, height, true);

        // Scales achievement box to new screen size
        float temp = dialogScaleFactor;
        dialogScaleFactor = (width / oldWidth);
        if (temp > dialogScaleFactor) {
            dialogScaleFactor = -dialogScaleFactor;
        }
        achievementBox.scaleBy(dialogScaleFactor - 1);
        oldWidth = width;
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    /** Releases all resources used by this screen. */
    @Override
    public void dispose() {
        player.getTexture().dispose();
        bottle.getTexture().dispose();
        keycard.getTexture().dispose();
        longBoi.getTexture().dispose();
        twig.dispose();
        beer.dispose();
        vodka.dispose();
        chicken.dispose();
        York.dispose();
        Lancaster.dispose();
        bob.dispose();
        birdSeed.dispose();
        stage.dispose();
        achievementBox.getContentTable().clearChildren();
        achievementBox.remove();

        if (musicToggle) {
            music.dispose();
        }
    }

    private void saveLeaderBoardScore(int score) {
        FileHandle writeFile = Gdx.files.local("leaderBoard.csv");
        writeFile.writeString(playerName + "," + Integer.toString(score) + "\n", true);
    }

    private void setAchievementText(String text) {
        eventTriggered = true;
        achievementBox.getContentTable().clearChildren();
        achievementBox.text(text);

        achievementData.put(text, true);
    }

    private void positionDialogueBox() {
        if (dialogScaleFactor < 0) {
            dialogScaleFactor = dialogScaleFactor * -1;
        }
        // Sets achievement box to bottom right
        float w = Gdx.graphics.getWidth() / 2;
        achievementBox.setPosition(w, 0);
        // Pads text to avoid truncation
        achievementBox.getContentTable().padLeft(4f);
        achievementBox.getContentTable().padRight(4f);
    }

    // Toggles music status between paused and play
    // If music is enabled
    public void toggleMusic() {
        if (musicToggle && !music.isPlaying()) {
            music.play();
        } else if (musicToggle) {
            music.pause();
        }
    }
}
