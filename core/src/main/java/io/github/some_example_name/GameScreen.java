package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final StumbleHome game;

    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    TiledMapTileLayer collisionLayer;

    // Map boundaries
    private final float mapWidth;
    private final float mapHeight;
    private final float minCameraX;
    private final float maxCameraX;
    private final float minCameraY;
    private final float maxCameraY;

    private boolean paused = false;
    private float remainingTime = 200f;
    private boolean timeUp = false;
    private boolean reachedFinish = false;
    private boolean hasKeycard = false;
    private boolean showNoKeycardMessage = false;
    private float noKeycardMessageTimer = 0f;
    private boolean showBottleMessage = false;
    private float bottleMessageTimer = 0f;
    private boolean showLongBoiMessage = false;
    private float longBoiMessageTimer = 0f;

    private final Player player;

    private final bottleEvent bottle;
    private final longBoiEvent longBoi;
    private final keycardEvent keycard;

    private int hiddenEventCounter = 0;
    private int helpfulEventCounter = 0;
    private int hinderingEventCounter = 0;

    public GameScreen(final StumbleHome game){
        this.game = game;

        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/16f);
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("hedge");

        // Get map properties FIRST
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

        //Initialize player
        player = new Player(new Sprite(new Texture("character.png")), mapWidth, mapHeight, collisionLayer);

        // Position player at start point of map
        player.playerX = mapWidth - 12  - player.playerSize / 2;
        player.playerY = mapHeight - 2 - player.playerSize / 2;

        //EVENTS
        bottle = new bottleEvent(new Sprite(new Texture("waterBottle.png")));
        bottle.bottleX = mapWidth - 3 - bottle.bottleSize / 2;
        bottle.bottleY = mapHeight - 23 - bottle.bottleSize / 2;

        longBoi = new longBoiEvent(new Sprite(new Texture("longBoi.png")));
        longBoi.longX = mapWidth - 23 - longBoi.longSize / 2;
        longBoi.longY = mapHeight - 38 - longBoi.longSize / 2;

        keycard = new keycardEvent(new Sprite(new Texture("keyCard.png")));
        keycard.keycardX = mapWidth  - 57 - keycard.keycardSize / 2;
        keycard.keycardY = mapHeight - 5 - keycard.keycardSize / 2;

        // Center camera on player position
        game.camera.position.set(mapWidth / 2, mapHeight / 2, 0);
    }


    @Override
    public void show() {
        // start playback of background music when screen is shown
    }

    @Override
    public void render(float delta) {
        // Toggle pause when SPACE is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            paused = !paused; // flip pause state
        }

        // Only run player input and logic if not paused and there is enough time left
        if (!paused && !timeUp) {
            input();
            logic();
        }

        draw();

        if (showNoKeycardMessage) {
            drawNoKeycardMessage();
        }
        if (showBottleMessage) {
            drawBottleMessage();
        }
        if (paused) {
            drawPauseOverlay();
        }
        if (showLongBoiMessage) {
            drawLongBoiMessage();
        }
        if (timeUp && !reachedFinish) {
            int score = (hiddenEventCounter + helpfulEventCounter + hinderingEventCounter) * 50;
            game.setScreen(new GameOverScreen(game, remainingTime, score));
        }
    }

    private void input() {
        player.input();
    }

    private void logic() {
        player.logic();

        //events logic
        bottle.logic();
        keycard.logic();
        longBoi.logic();

        float playerCentreX = player.playerX + player.playerSize / 2;
        float playerCentreY = player.playerY + player.playerSize / 2;

        // Check collision between player and water bottle
        if (bottle.checkCollision(playerCentreX, playerCentreY, player.playerSize)) {
            // Player collected the water bottle - becomes sober
            player.isDrunk = 0;
            helpfulEventCounter++;
            showBottleMessage = true;
            bottleMessageTimer = 3f;
        }

        // check collision with keycard
        if (keycard.checkCollision(playerCentreX, playerCentreY, player.playerSize)) {
            hasKeycard = true;
            hinderingEventCounter++;
            drawKeycardMessage();
        }

        if (!longBoi.doneWalk) {
            // check if player near longBoi
            if (longBoi.checkNear(playerCentreX, playerCentreY)) {
                // player is near
                showLongBoiMessage = true;
                longBoiMessageTimer = 2f;
                hiddenEventCounter++;
            }

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
        if (!paused && !reachedFinish) {
            if (remainingTime > 0) {
                remainingTime -= Gdx.graphics.getDeltaTime();
                if (remainingTime <= 0) {
                    remainingTime = 0;
                    timeUp = true;
                }
            }

        }


        float finishZoneX = 0f;
        float finishZoneY = 0f;
        float finishZoneWidth = 7f;    // 3 tiles wide
        float finishZoneHeight = 7f;   // 3 tiles tall


        if (!reachedFinish && hasKeycard &&
            player.playerX < finishZoneX + finishZoneWidth &&
            player.playerX + player.playerSize > finishZoneX &&
            player.playerY < finishZoneY + finishZoneHeight &&
            player.playerY + player.playerSize > finishZoneY) {
            timeUp = true;
            paused = true;
            int score = (int) (remainingTime * 10) + (hiddenEventCounter + helpfulEventCounter + hinderingEventCounter) * 50;
            reachedFinish = true;
            game.setScreen(new WinScreen(game, remainingTime, score));
            dispose();
        }

        if (!hasKeycard &&
            player.playerX < finishZoneX + finishZoneWidth &&
            player.playerX + player.playerSize > finishZoneX &&
            player.playerY < finishZoneY + finishZoneHeight &&
            player.playerY + player.playerSize > finishZoneY) {

            showNoKeycardMessage = true;
            noKeycardMessageTimer = 3f;
        }

        if (showNoKeycardMessage) {
            noKeycardMessageTimer -= Gdx.graphics.getDeltaTime();
            if (noKeycardMessageTimer <= 0) {
                showNoKeycardMessage = false;
            }
        }

        if (showBottleMessage) {
            bottleMessageTimer -= Gdx.graphics.getDeltaTime();
            if (bottleMessageTimer <= 0) {
                showBottleMessage = false;
            }
        }

        if (showLongBoiMessage) {
            longBoiMessageTimer -= Gdx.graphics.getDeltaTime();
                if (longBoiMessageTimer <= 0) {
                    showLongBoiMessage = false;
                }
        }


        clampCamera();
    }

    private void clampCamera() {
        // Only clamp if map is larger than viewport in each dimension
        if (mapWidth >= game.viewport.getWorldWidth()) {
            game.camera.position.x = MathUtils.clamp(
                game.camera.position.x,
                minCameraX,
                maxCameraX
            );
        }
        if (mapHeight >= game.viewport.getWorldHeight()) {
            game.camera.position.y = MathUtils.clamp(
                game.camera.position.y,
                minCameraY,
                maxCameraY
            );
        }
    }

    private void drawCenteredText(String text, Color color, float scale) {
        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        game.batch.begin();
        game.font.getData().setScale(scale);
        game.font.setColor(color);

        GlyphLayout layout = new GlyphLayout(game.font, text);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2f;
        game.font.draw(game.batch, layout, x, y);

        game.font.getData().setScale(1f);
        game.batch.end();
    }

    private void draw() {
        // Clear the screen with black color
        ScreenUtils.clear(Color.BLACK);

        game.camera.update();

        renderer.setView(game.camera);
        renderer.render();

        // Set batch to use camera's coordinate system
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();

        // Then draw player
        player.draw(game.batch);
        bottle.draw(game.batch);
        if (!longBoi.doneWalk & longBoi.getNear()) {
            longBoi.draw(game.batch);
        }
        keycard.draw(game.batch);

        game.batch.end();

        // switch to screen coordinates for UI
        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        int minutes = (int)(remainingTime / 60);
        int seconds = (int)(remainingTime % 60);
        String timeText = String.format("%02d:%02d", minutes, seconds);

        int totalEvents = 5;
        String helpfulEventText = "Helpful events: " + helpfulEventCounter + "/" + totalEvents;
        String hinderingEventText = "Hindering events: " + hinderingEventCounter + "/" + totalEvents;
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

    private void drawPauseOverlay() {
        drawCenteredText("PAUSED", Color.WHITE, 3f);
    }

    private void drawNoKeycardMessage() {
        drawCenteredText("You need a KeyCard to enter the Door...", Color.RED, 3f);
    }

    private void drawBottleMessage() {
        drawCenteredText("You remembered that you don't have the keycard, find it!", Color.YELLOW, 2f);
    }

    private void drawKeycardMessage() {
        drawCenteredText("You have the keyCard, you can go home now!", Color.YELLOW, 2f);}

    private void drawLongBoiMessage() {
        drawCenteredText("It's Long Boi! Avoid him!", Color.RED, 3f);
    }


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

    @Override
    public void dispose() {
        player.getTexture().dispose();
        bottle.getTexture().dispose();
        keycard.getTexture().dispose();
        longBoi.getTexture().dispose();
    }
}

