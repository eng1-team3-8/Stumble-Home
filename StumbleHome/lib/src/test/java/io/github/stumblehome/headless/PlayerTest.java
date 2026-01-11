package io.github.stumblehome.headless;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.github.stumblehome.Entities.EntityDirection;
import io.github.stumblehome.Entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest extends AbstractHeadlessGdxTest {
    // Initianalize the player for using in each Test
    public Player testUnit;

    @BeforeEach
    public void setUp() {

        // E
        TiledMap map = new TmxMapLoader().load("map2.tmx");

        // Set up map size to be the same as the game
        int mapWidthInTiles = map.getProperties().get("width", Integer.class);
        int mapHeightInTiles = map.getProperties().get("height", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = map.getProperties().get("tileheight", Integer.class);

        // Calculate map dimensions in world units (remember: 1 world unit = 16 pixels)
        float mapWidth = mapWidthInTiles * tilePixelWidth / 16f;
        float mapHeight = mapHeightInTiles * tilePixelHeight / 16f;

        // Load collision layers
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("hedge");
        // Create player for test
        testUnit =
                new Player(
                        new Texture("character.png"),
                        0.8f,
                        new float[] {0f, 0f},
                        mapWidth,
                        mapHeight,
                        collisionLayer);
    }

    @Test
    public void testCollisionWithMazeWall() {
        // Assiging test unit
        Player player = testUnit;

        // Test moving to coords without wall
        assertTrue(player.canMoveTo(1, 1));
        // Base of player collides with top of wall
        assertFalse(player.canMoveTo(56f, 45.3f));

        // Right side of player collides with left of wall
        assertFalse(player.canMoveTo(61.4f, 49f));

        // Left side of player collides with right of wall
        assertFalse(player.canMoveTo(54.7f, 49f));

        // Top side of player collides with bottom of wall
        assertFalse(player.canMoveTo(60f, 43.4f));
    }

    @Test
    public void testDrunkAndSoberMethods() {

        Player player = testUnit;

        assertTrue(
                player.isPlayerDrunk(),
                "Player class attribute isDrunk should have the value True");
        assertEquals(-5f, player.getPlayerSpeed(), "Speed should be reversed when drunk");
        player.setSober();
        assertFalse(
                player.isPlayerDrunk(),
                "Player class method SwapControl must reverse isDrunk value");
        assertEquals(5f, player.getPlayerSpeed(), "Speed is not reversing");
        player.setDrunk();
        assertTrue(player.isPlayerDrunk(), "setDrunk method should return True value for isDrunk");
        assertEquals(-5f, player.getPlayerSpeed(), "setDrunk should result in a negative speed");
    }

    @Test
    public void testSlowDown() {
        Player player = testUnit;
        boolean result;
        result = player.slowDownPlayer(4);
        assertTrue(result, "Player should report success");
        assertEquals(-1f, player.getPlayerSpeed(), "player should be slowed down");
        result = player.slowDownPlayer(1);
        assertEquals(0, player.getPlayerSpeed(), "player should be able to be slowed down to 0");
        assertTrue(result, "Player should report success");
        result = player.slowDownPlayer(2);
        assertEquals(
                0f, player.getPlayerSpeed(), "player should not be able to be slowed down past 0");
        assertFalse(result, "Player should report failure");
    }

    @Test
    public void testSpeedUp() {
        Player player = testUnit;

        player.speedUpPlayer(6);
        assertEquals(-11f, player.getPlayerSpeed());

        player.setSober();
        player.speedUpPlayer(5);
        assertEquals(16f, player.getPlayerSpeed());
    }

    @Test
    public void testConstructorInitialization() {
        Player player = testUnit;

        // Test initial state after constructor
        assertEquals(0f, player.getX(), "Player X should start at 0");
        assertEquals(0f, player.getY(), "Player Y should start at 0");
        assertEquals(0.8f, player.frame_size, "Player size should be 0.8");
        assertTrue(player.isPlayerDrunk(), "Player should be drunk initially");
        assertEquals(-5f, player.getPlayerSpeed(), "Player speed should be -5 when drunk");
    }

    @Test
    public void testSlowDownWhenSober() {
        Player player = testUnit;

        // Make player sober
        player.setSober();
        assertEquals(5f, player.getPlayerSpeed(), "Should be sober with positive speed");

        // Slow down when sober (normal behavior)
        player.slowDownPlayer(3);
        assertEquals(2f, player.getPlayerSpeed(), "SlowDown should work normally when sober");
    }

    @Test
    public void testSpeedUpWhenSober() {
        Player player = testUnit;

        // Make player sober
        player.setSober();
        assertEquals(5f, player.getPlayerSpeed(), "Should be sober with positive speed");

        // Speed up when sober (normal behavior)
        player.speedUpPlayer(3);
        assertEquals(8f, player.getPlayerSpeed(), "SpeedUp should work normally when sober");
    }

    @Test
    public void testMultipleSwapControls() {
        Player player = testUnit;

        // Test multiple swaps
        assertTrue(player.isPlayerDrunk(), "initally player should be drunk");
        assertEquals(-5f, player.getPlayerSpeed(), "Initally speed should be negative");

        player.setSober();
        player.setDrunk();
        player.setSober();
        assertFalse(player.isPlayerDrunk(), "multiple switches of setSober, setDrunk should work");
        assertEquals(5f, player.getPlayerSpeed(), "speed when sober should be positive");
    }

    @Test
    public void testCanMoveToBoundaries() {
        Player player = testUnit;

        // Test at origin (should be valid if no collision)
        assertTrue(player.canMoveTo(1f, 1f), "Origin should be valid if no collision");

        // Test with very small positions
        assertTrue(player.canMoveTo(1.1f, 1.1f), "Small positive positions should be valid");
    }

    @Test
    public void testInputNoInput() {
        Player player = testUnit;
        player.input();
        assertEquals(player.getX(), 0, "Player should not move with no input");
        assertEquals(player.getY(), 0, "Player should not move with no input");
    }

    @Test
    public void testSetDirection() {
        Player player = testUnit;
        player.setSober();
        assertEquals(
                player.getDirection(),
                EntityDirection.UP,
                "intial direction should be up when sober");
        player.input();
        assertEquals(
                player.getDirection(),
                EntityDirection.UP,
                "input with no input should not change direction");
        player.changeDirection(0);
        assertEquals(
                player.getDirection(),
                EntityDirection.DOWN,
                "change direction should be set to down when sober");
        player.changeDirection(1);
        assertEquals(
                player.getDirection(),
                EntityDirection.LEFT,
                "change direction should be set to left when sober");
        player.changeDirection(2);
        assertEquals(
                player.getDirection(),
                EntityDirection.UP,
                "change direction should be set to up when sober");
        player.changeDirection(3);
        assertEquals(
                player.getDirection(),
                EntityDirection.RIGHT,
                "change direction should be set to right when sober");
    }

    @Test
    public void testSetDirectionDrunk() {
        Player player = testUnit;
        assertEquals(
                player.getDirection(), EntityDirection.DOWN, "intial direction should be down");
        player.input();
        assertEquals(
                player.getDirection(),
                EntityDirection.DOWN,
                "input with no input should not change direction");
        player.changeDirection(0);
        assertEquals(
                player.getDirection(), EntityDirection.UP, "change direction should be set to up");
        player.changeDirection(1);
        assertEquals(
                player.getDirection(),
                EntityDirection.RIGHT,
                "change direction should be set to right");
        player.changeDirection(2);
        assertEquals(
                player.getDirection(),
                EntityDirection.DOWN,
                "change direction should be set to down");
        player.changeDirection(3);
        assertEquals(
                player.getDirection(),
                EntityDirection.LEFT,
                "change direction should be set to left");
    }
}
