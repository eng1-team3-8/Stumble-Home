package io.github.stumblehome.headless.EventTests;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.github.stumblehome.Entities.Chainsaw;
import io.github.stumblehome.Entities.EntityDirection;
import io.github.stumblehome.Entities.Player;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChainsawRemoveWallTests extends AbstractHeadlessGdxTest {
    private Chainsaw chainsaw;
    private Player testPlayer;
    private TiledMapTileLayer testCollisionLayer;
    private TiledMapTileLayer initalCollisionLayer;
    private EntityDirection direction;
    private int[] pos;

    @BeforeEach
    public void setUp() {
        // Load Map and get collision layers
        TiledMap testMap = new TmxMapLoader().load("map2.tmx");
        testCollisionLayer = (TiledMapTileLayer) testMap.getLayers().get("hedge");
        TiledMap testMap2 = new TmxMapLoader().load("map2.tmx");
        initalCollisionLayer = (TiledMapTileLayer) testMap2.getLayers().get("hedge");

        // Set up map size to be the same as the game
        int mapWidthInTiles = testMap.getProperties().get("width", Integer.class);
        int mapHeightInTiles = testMap.getProperties().get("height", Integer.class);
        int tilePixelWidth = testMap.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = testMap.getProperties().get("tileheight", Integer.class);
        // Calculate map dimensions in world units (remember: 1 world unit = 16 pixels)
        float mapWidth = mapWidthInTiles * tilePixelWidth / 16f;
        float mapHeight = mapHeightInTiles * tilePixelHeight / 16f;
        // Initialise player object
        testPlayer =
                new Player(
                        new Texture("character.png"),
                        0.8f,
                        new float[] {0f, 0f},
                        mapWidth,
                        mapHeight,
                        testCollisionLayer);

        chainsaw = new Chainsaw(new Texture(Chainsaw.ASSET), 1f, new float[] {0f, 0f});
    }

    @Test
    public void testRemoveWallStripNoWall() {
        pos = new int[] {1, 1};
        direction = EntityDirection.UP;
        assertNull(
                testCollisionLayer.getCell(pos[0], pos[1]),
                "this cell should be empty else test won't work");
        Chainsaw.remove_wall_strip(testCollisionLayer, pos, direction);
        assertNull(
                testCollisionLayer.getCell(pos[0], pos[1]),
                "this cell should not be changed by the function");
    }

    @Test
    public void testRemoveWallStripSingleWall() {
        pos = new int[] {4, 13};
        direction = EntityDirection.RIGHT;
        assertNotNull(
                testCollisionLayer.getCell(pos[0], pos[1]),
                "this cell should be filled else test won't work");
        Chainsaw.remove_wall_strip(testCollisionLayer, pos, direction);
        assertNull(
                testCollisionLayer.getCell(pos[0], pos[1]), "this cell should have been removed");
    }

    @Test
    public void testRemoveWallStripWallLine() {
        pos = new int[] {0, 0};
        direction = EntityDirection.UP;
        for (int count = 0; count < testCollisionLayer.getHeight(); count++) {
            assertNotNull(testCollisionLayer.getCell(pos[0], pos[1] + count));
        }
        Chainsaw.remove_wall_strip(testCollisionLayer, pos, direction);
        for (int count = 0; count < testCollisionLayer.getHeight(); count++) {
            assertNull(testCollisionLayer.getCell(pos[0], pos[1] + count));
        }
    }

    @Test
    public void useWhenNotCollected() {
        testPlayer.changeDirection(1);
        assertFalse(
                chainsaw.UseChainsaw(testPlayer, testCollisionLayer),
                "Chainsaw should not be used when it has not been collected");
        assertNotNull(
                testCollisionLayer.getCell(0, 1),
                "Chainsaw should not have an effect if it has not been collected");
    }

    @Test
    public void useWhenCollected() {
        testPlayer.changeDirection(0);
        chainsaw.isCollected = true;
        assertTrue(
                chainsaw.UseChainsaw(testPlayer, testCollisionLayer),
                "Chainsaw should be used when it has not been collected");
        assertNull(testCollisionLayer.getCell(0, 1), "Chainsaw should have an effect when used");
    }

    @Test
    public void usetwice() {
        testPlayer.changeDirection(0);
        chainsaw.isCollected = true;
        chainsaw.UseChainsaw(testPlayer, testCollisionLayer);
        testPlayer.setX(2);
        testPlayer.changeDirection(1);
        chainsaw.UseChainsaw(testPlayer, testCollisionLayer);
        assertFalse(
                chainsaw.UseChainsaw(testPlayer, testCollisionLayer),
                "Chainsaw should not be able to be used twice");
        assertNotNull(
                testCollisionLayer.getCell(3, 0),
                "Chainsaw should not have an effect if it has been used already");
    }
}
