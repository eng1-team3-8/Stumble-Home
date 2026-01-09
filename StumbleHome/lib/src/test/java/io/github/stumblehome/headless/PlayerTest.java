package io.github.stumblehome.headless;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.github.stumblehome.Entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest extends AbstractHeadlessGdxTest {
    // Initianalize the player for using in each Test
    public Player testUnit;

    @BeforeEach
    public void setUp() {

        // E
        TiledMap map = new TmxMapLoader().load("map.tmx");

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
        Player player =
                new Player(
                        new Texture(Player.ASSET),
                        0.8f,
                        new float[] {0f, 0f},
                        mapWidth,
                        mapHeight,
                        collisionLayer);

        testUnit = player;
    }

    @Test
    public void testCollisionWithMazeWall() {
        // Assiging test unit
        Player player = testUnit;

        // Test moving to coords without wall
        assertTrue(player.canMoveTo(2, 2));

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
    public void testSwapControls() {

        Player player = testUnit;

        assertTrue(player.isDrunk, "Player class attribute isDrunk should have the value True");
        player.SwapControls();
        assertFalse(player.isDrunk, "Player class method SwapControl must reverse isDrunk value");
        assertEquals(5f, player.player_speed, "Speed is not reversing");
        player.SwapControls();
        assertTrue(
                player.isDrunk, "Double SwapControls method should return True value for isDrunk");
        assertEquals(-5f, player.player_speed);
    }

    @Test
    public void testSlowDown() {
        Player player = testUnit;

        player.slowDownPlayer(4);
        assertEquals(-1f, player.player_speed);
        player.slowDownPlayer(1);
        assertEquals(0, player.player_speed);
        player.slowDownPlayer(1);
        assertEquals(1f, player.player_speed);
    }

    @Test
    public void testSpeedUp() {
        Player player = testUnit;

        player.speedUpPlayer(6);
        assertEquals(-11f, player.player_speed);

        player.SwapControls();
        player.speedUpPlayer(5);
        assertEquals(16f, player.player_speed);
    }

    @Test
    public void testConstructorInitialization() {
        Player player = testUnit;

        // Test initial state after constructor
        assertEquals(0f, player.getX(), "Player X should start at 0"); // Was player.playerX
        assertEquals(0f, player.getY(), "Player Y should start at 0"); // Was player.playerY
        assertEquals(0.8f, player.frame_size, "Player size should be 0.8"); // Was player.playerSize
        assertTrue(player.isDrunk, "Player should be drunk initially");
        assertEquals(-5f, player.player_speed, "Player speed should be -5 when drunk");
    }

    @Test
    public void testSlowDownWhenSober() {
        Player player = testUnit;

        // Make player sober
        player.SwapControls();
        assertEquals(5f, player.player_speed, "Should be sober with positive speed");

        // Slow down when sober (normal behavior)
        player.slowDownPlayer(3);
        assertEquals(2f, player.player_speed, "SlowDown should work normally when sober");
    }

    @Test
    public void testSpeedUpWhenSober() {
        Player player = testUnit;

        // Make player sober
        player.SwapControls();
        assertEquals(5f, player.player_speed, "Should be sober with positive speed");

        // Speed up when sober (normal behavior)
        player.speedUpPlayer(3);
        assertEquals(8f, player.player_speed, "SpeedUp should work normally when sober");
    }

    @Test
    public void testMultipleSwapControls() {
        Player player = testUnit;

        // Test multiple swaps
        assertTrue(player.isDrunk);
        assertEquals(-5f, player.player_speed);

        player.SwapControls();
        assertFalse(player.isDrunk);
        assertEquals(5f, player.player_speed);

        player.SwapControls();
        assertTrue(player.isDrunk);
        assertEquals(-5f, player.player_speed);

        player.SwapControls();
        assertFalse(player.isDrunk);
        assertEquals(5f, player.player_speed);
    }

    @Test
    public void testCanMoveToBoundaries() {
        Player player = testUnit;

        // Test at origin (should be valid if no collision)
        assertTrue(player.canMoveTo(0f, 0f), "Origin should be valid if no collision");

        // Test with very small positions
        assertTrue(player.canMoveTo(0.1f, 0.1f), "Small positive positions should be valid");
    }
}
