package io.github.stumblehome.headless.EventTests;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.github.stumblehome.Entities.LongBoiEvent;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LongBoiEventTest extends AbstractHeadlessGdxTest {
    private LongBoiEvent longBoi;
    private TiledMapTileLayer collisionLayer;

    @BeforeEach
    public void setUp() {
        TiledMap map = new TmxMapLoader().load("map.tmx");
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("hedge");
        longBoi =
                new LongBoiEvent(
                        new Texture("character.png"), 2f, new float[] {1f, 1f}, collisionLayer);
    }

    @Test
    public void testConstructor() {
        assertNotNull(longBoi);
        assertEquals(2f, longBoi.frame_size);
    }

    @Test
    public void testFlagsInitialized() {
        assertFalse(longBoi.getNear());
        assertFalse(longBoi.collided);
        assertFalse(longBoi.done_walk);
    }

    @Test
    public void testLongBoiPositionInitialized() {
        longBoi.setX(10.0f);
        longBoi.setY(15.0f);

        assertEquals(10.0f, longBoi.getX());
        assertEquals(15.0f, longBoi.getY());
    }

    @Test
    public void testCheckNearWhenClose() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        boolean result = longBoi.checkNear(6.0f, 6.0f);

        assertTrue(result);
    }

    @Test
    public void testCheckNearWhenFar() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        boolean result = longBoi.checkNear(15.0f, 15.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckNearPersists() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        longBoi.checkNear(6.0f, 6.0f);
        boolean nearAfterClose = longBoi.getNear();
        longBoi.checkNear(15.0f, 15.0f);
        boolean nearAfterFar = longBoi.getNear();

        assertTrue(nearAfterClose);
        assertTrue(nearAfterFar);
    }

    @Test
    public void testCheckCollisionWhenClose() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        boolean result = longBoi.checkColliding(6.0f, 6.0f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionWhenFar() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        boolean result = longBoi.checkColliding(10.0f, 10.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionSetsCollided() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        boolean firstCollision = longBoi.checkColliding(6.0f, 6.0f);
        boolean secondCheck = longBoi.checkColliding(10.0f, 10.0f);

        assertTrue(firstCollision);
        assertTrue(secondCheck);
    }

    @Test
    public void testLogicUpdatesState() {
        longBoi.logic();

        assertNotNull(longBoi);
    }

    @Test
    public void testLogicWhenNear() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);
        longBoi.checkNear(6.0f, 6.0f);

        longBoi.logic();

        assertNotNull(longBoi);
    }

    @Test
    public void testWalkPathWorks() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);
        longBoi.checkNear(6.0f, 6.0f);

        longBoi.walkPath();

        assertNotNull(longBoi);
    }

    @Test
    public void testRadiusProperty() {
        assertEquals(6f, longBoi.getRadius());

        longBoi.setRadius(3.0f);

        assertEquals(3.0f, longBoi.getRadius());
    }

    @Test
    public void testDoneWalkProperty() {
        assertFalse(longBoi.done_walk);

        longBoi.done_walk = true;

        assertTrue(longBoi.done_walk);
    }

    @Test
    public void testCollidedProperty() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);
        longBoi.checkColliding(6.0f, 6.0f);

        assertTrue(longBoi.collided);
    }

    @Test
    public void testLongBoiTextureInitialized() {
        assertNotNull(longBoi.getTexture());
    }

    @Test
    public void testMultipleLongBoisIndependent() {
        LongBoiEvent l1 =
                new LongBoiEvent(
                        new Texture("character.png"), 1f, new float[] {0f, 0f}, collisionLayer);
        LongBoiEvent l2 =
                new LongBoiEvent(
                        new Texture("character.png"), 1f, new float[] {20f, 20f}, collisionLayer);

        l1.checkNear(1.0f, 1.0f);
        boolean l1Near = l1.getNear();
        boolean l2Near = l2.getNear();

        assertTrue(l1Near);
        assertFalse(l2Near);
    }

    @Test
    public void testNegativeCoordinates() {
        longBoi.setX(-5.0f);
        longBoi.setY(-5.0f);

        longBoi.logic();

        assertNotNull(longBoi);
    }

    @Test
    public void testLargeCoordinates() {
        longBoi.setX(1000.0f);
        longBoi.setY(2000.0f);

        longBoi.logic();

        assertNotNull(longBoi);
    }

    @Test
    public void testGameScenarioLongBoiEncounter() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        longBoi.logic();
        assertFalse(longBoi.getNear());

        longBoi.logic();
        boolean nearCheck = longBoi.checkNear(6.0f, 6.0f);
        assertTrue(nearCheck);
        assertTrue(longBoi.getNear());

        longBoi.logic();
        longBoi.walkPath();
        assertFalse(longBoi.checkColliding(10.0f, 10.0f));

        boolean collision = longBoi.checkColliding(6.0f, 6.0f);
        assertTrue(collision);
        assertTrue(longBoi.collided);
    }

    @Test
    public void testMultipleLongBoisGameplay() {
        LongBoiEvent l1 =
                new LongBoiEvent(
                        new Texture("character.png"), 1f, new float[] {0f, 0f}, collisionLayer);
        LongBoiEvent l2 =
                new LongBoiEvent(
                        new Texture("character.png"), 1f, new float[] {10f, 10f}, collisionLayer);

        l1.logic();
        boolean l1Near = l1.checkNear(1.0f, 1.0f);
        l2.logic();
        boolean l2Near = l2.checkNear(11.0f, 11.0f);
        boolean l1Collision = l1.checkColliding(1.0f, 1.0f);
        boolean l2Collision = l2.checkColliding(11.0f, 11.0f);

        assertTrue(l1Near);
        assertTrue(l2Near);
        assertTrue(l1Collision);
        assertTrue(l2Collision);
    }

    @Test
    public void testLongBoiMovementAndLogic() {
        longBoi.setX(0f);
        longBoi.setY(0f);

        longBoi.logic();
        boolean nearAfterFirstLogic = longBoi.getNear();
        longBoi.setX(10.0f);
        longBoi.setY(10.0f);
        longBoi.logic();

        assertFalse(nearAfterFirstLogic);
        assertNotNull(longBoi);
    }

    @Test
    public void testAnimationAndNearTiming() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        for (int i = 0; i < 5; i++) {
            longBoi.logic();
            assertFalse(longBoi.getNear());
        }

        longBoi.checkNear(6.0f, 6.0f);
        assertTrue(longBoi.getNear());

        for (int i = 0; i < 5; i++) {
            longBoi.logic();
            longBoi.walkPath();
            assertTrue(longBoi.getNear());
        }
    }

    @Test
    public void testLongBoiWithDoneWalk() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);
        longBoi.checkNear(6.0f, 6.0f);

        longBoi.logic();
        longBoi.walkPath();
        longBoi.done_walk = true;

        assertTrue(longBoi.done_walk);
        longBoi.logic();
        assertNotNull(longBoi);
    }

    @Test
    public void testComplexLongBoiInteraction() {
        longBoi.setX(5.0f);
        longBoi.setY(5.0f);

        longBoi.logic();
        boolean near = longBoi.checkNear(6.0f, 6.0f);
        assertTrue(near);

        longBoi.logic();
        longBoi.walkPath();
        assertTrue(longBoi.getNear());

        longBoi.done_walk = true;
        assertTrue(longBoi.done_walk);

        longBoi.logic();
        assertTrue(longBoi.getNear());
    }
}
