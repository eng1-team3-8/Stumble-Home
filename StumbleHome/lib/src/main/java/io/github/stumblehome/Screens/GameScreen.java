package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.stumblehome.*;
import io.github.stumblehome.Messages.MessageHandler;
import io.github.stumblehome.Messages.Messages;

/**
 * The {@code GameScreen} class represents the main gameplay screen in the StumbleHome game.
 * <p>
 * It handles player movement, camera control, event interactions, time tracking,
 * and determines win or loss conditions.
 * </p>
 *
 * <p>The class uses LibGDX's {@link Screen} interface to define game lifecycle
 * behavior such as rendering, resizing, and disposal.</p>
 *
 * <p>In this screen:
 * <ul>
 *   <li>The player navigates through a tiled map to reach the finish zone.</li>
 *   <li>Various events (e.g., bottle, keycard, Long Boi) influence the gameplay.</li>
 *   <li>A timer counts down, ending the game when it reaches zero.</li>
 * </ul>
 * </p>
 */
public class GameScreen implements Screen {
    // Reference to the main game instance.
    final StumbleHome game;

    // The current map being rendered.
    TiledMap map;

    // Renders the tiled map using an orthogonal projection.
    OrthogonalTiledMapRenderer renderer;

    // Collision layer representing obstacles (e.g., hedges).
    TiledMapTileLayer collisionLayer;

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

    // Indicates whether the game is currently paused.
    private boolean paused = false;

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

    // Chicken for the positive event
    private final Food chicken;

    // Counters for hidden, helpful, and hindering events.
    private int hiddenEventCounter = 0;
    private int helpfulEventCounter = 0;
    private int hinderingEventCounter = 0;

    private float time;
    private final String playerName;

    /**
     * Constructs the {@code GameScreen} and initializes the map, player, camera,
     * and in-game events.
     *
     * @param game the main {@link StumbleHome} game instance.
     */
    public GameScreen(final StumbleHome game, final String playerName) {
        this.game = game;
        this.playerName = playerName;

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
                        new Sprite(new Texture("character.png")),
                        mapWidth,
                        mapHeight,
                        collisionLayer);

        // Position player at start point of map
        player.playerX = mapWidth - 12 - player.playerSize / 2;
        player.playerY = mapHeight - 2 - player.playerSize / 2;

        // Center camera on player position
        game.camera.position.set(mapWidth / 2, mapHeight / 2, 0);

        // events
        // Bottle event (not reimplemented yet)
        bottle = new BottleEvent(new Sprite(new Texture("waterBottle.png")));
        bottle.bottleX = mapWidth - 3 - bottle.bottleSize / 2;
        bottle.bottleY = mapHeight - 23 - bottle.bottleSize / 2;

        // LongBoi event
        longBoi = new LongBoiEvent(new Sprite(new Texture("longBoi.png")), collisionLayer);
        longBoi.longX = mapWidth - 23 - longBoi.longSize / 2;
        longBoi.longY = mapHeight - 38 - longBoi.longSize / 2;

        // Keycard event (not reimplemented yet)
        keycard = new KeycardEvent(new Sprite(new Texture("keyCard.png")));
        keycard.keycardX = mapWidth - 57 - keycard.keycardSize / 2;
        keycard.keycardY = mapHeight - 5 - keycard.keycardSize / 2;

        // Twig event
        twig = new Twig(new Texture("Sprites/Stick.png"), 1f, new float[] {51f, 19f});

        // Beer bottle
        beer = new Alcohol(new Texture("Sprites/Tsingtao.png"), 1f, new float[] {62f, 5f});

        // Vodka bottle
        vodka = new Alcohol(new Texture("Sprites/Smirnoff.png"), 1f, new float[] {35f, 2f});

        // Chicken
        chicken = new Food(new Texture("Sprites/Chicken.png"), 1f, new float[] {54f, 42f});

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
    }

    @Override
    public void show() {}

    /**
     * Called once per frame to update and render the game state.
     * <p>
     * Handles player input, game logic, event interactions, and draws
     * all entities and UI elements.
     * </p>
     *
     * @param delta the time (in seconds) since the last render.
     */
    @Override
    public void render(float delta) {
        // Increments the cloack
        this.time += Gdx.graphics.getDeltaTime();

        // Toggle pause when SPACE is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            paused = !paused; // flip pause state
        }

        // Only run player input and logic if not paused and there is enough time left
        if (!paused && !timeUp) {
            player.input();
            logic();
        }
        draw();

        msg.updateMessages();
        msg.updateMessage(paused, Messages.PAUSED);

        if (timeUp && !reachedFinish) {
            int score = (hiddenEventCounter + helpfulEventCounter + hinderingEventCounter) * 50;
            game.setScreen(new GameOverScreen(game, remainingTime, score));
        }
    }

    /**
     * Handles core game logic, including player movement, event interactions,
     * collision detection, and time management.
     */
    private void logic() {
        player.logic();

        // events logic
        bottle.logic();
        keycard.logic();
        longBoi.logic();

        float playerCentreX = player.playerX + player.playerSize / 2;
        float playerCentreY = player.playerY + player.playerSize / 2;

        // Check collision between player and water bottle
        if (bottle.checkCollision(playerCentreX, playerCentreY)) {
            // Player collected the water bottle - becomes sober
            if (player.isDrunk) {
                player.SwapControls();
                msg.set_time(Messages.REMEMBERKEYCARD, 3f);
            }
            player.isDrunk = false;
            helpfulEventCounter++;
        }

        // check collision with keycard
        if (keycard.checkCollision(playerCentreX, playerCentreY)) {
            hasKeycard = true;
            hinderingEventCounter++;
            msg.set_time(Messages.PICKUPKEYCARD, 3f);
        }

        // Check collision with twig
        if (twig.checkColliding(playerCentreX, playerCentreY)) {
            hinderingEventCounter++;
            player.slowDownPlayer(.5f);
        }

        // Check if collision with beer
        if (beer.checkColliding(playerCentreX, playerCentreY)) {
            hinderingEventCounter++;
            beer.makeDrunk(player);
        }

        // Check if collision with vodka
        if (vodka.checkColliding(playerCentreX, playerCentreY)) {
            hinderingEventCounter++;
            vodka.makeDrunk(player);
        }

        // Check if collision with chicken
        if (chicken.checkColliding(playerCentreX, playerCentreY)) {
            helpfulEventCounter++;
            if (player.isDrunk) {
                msg.set_time(Messages.REMEMBERKEYCARD, 3f);
            }
            chicken.eatFood(player);
        }

        // if long boi walk not completed, run logic
        if (!longBoi.doneWalk) {
            // check if player near longBoi
            if (longBoi.checkNear(playerCentreX, playerCentreY)) {
                // player is near
                msg.set_time(Messages.LONGBOIAPPEAR, 2f);
                hiddenEventCounter++;
            }

            // walk long boi as long as 'near' variable set to true
            if (longBoi.getNear()) {
                longBoi.walkPath();
            }

            // check if player collided with longBoi
            if (longBoi.checkCollision(playerCentreX, playerCentreY)) {
                int score = (hiddenEventCounter + helpfulEventCounter + hinderingEventCounter) * 50;
                game.setScreen(new GameOverScreen(game, remainingTime, score));
            }
        }

        // Make camera follow player
        // Camera should be centered on player (add half player size to get center)
        float playerCenterX = player.playerX + player.playerSize / 2;
        float playerCenterY = player.playerY + player.playerSize / 2;

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
            game.setScreen(new WinScreen(game, remainingTime, score));
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
        return player.playerX < finishZoneX + finishZoneWidth
                && player.playerX + player.playerSize > finishZoneX
                && player.playerY < finishZoneY + finishZoneHeight
                && player.playerY + player.playerSize > finishZoneY;
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
        twig.drawEntity(game.batch);
        beer.drawEntity(game.batch);
        vodka.drawEntity(game.batch);
        chicken.drawEntity(game.batch);

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

        int totalEvents = 5;
        String helpfulEventText = "Helpful events: " + helpfulEventCounter + "/" + totalEvents;
        String hinderingEventText =
                "Hindering events: " + hinderingEventCounter + "/" + totalEvents;
        String hiddenEventText = "Hidden events: " + hiddenEventCounter + "/" + totalEvents;

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
     * @param width  the new width in pixels.
     * @param height the new height in pixels.
     */
    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
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
    }

    private void saveLeaderBoardScore(int score) {
        FileHandle writeFile = Gdx.files.local("leaderBoard.csv");
        writeFile.writeString(playerName + "," + Integer.toString(score) + "\n", true);
    }
    ;
}
