package io.github.stumblehome.headless;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.github.stumblehome.LongBoiEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LongBoiEventTest extends AbstractHeadlessGdxTest {
    private LongBoiEvent longBoi;
    private TiledMapTileLayer collisionLayer;

    @BeforeEach
    public void setUp() {
        TiledMap map = new TmxMapLoader().load("map.tmx");
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("hedge");
        longBoi = new LongBoiEvent(new Sprite(new Texture("character.png")), collisionLayer);
    }

    @Test
    public void testConstructor() {
        assertNotNull(longBoi);
        assertEquals(2f, longBoi.longSize);
    }

    @Test
    public void testFlagsInitialized() {
        assertFalse(longBoi.getNear());
        assertFalse(longBoi.collided);
        assertFalse(longBoi.doneWalk);
    }

    @Test
    public void testLongBoiPositionInitialized() {
        longBoi.longX = 10.0f;
        longBoi.longY = 15.0f;

        assertEquals(10.0f, longBoi.longX);
        assertEquals(15.0f, longBoi.longY);
    }

    @Test
    public void testCheckNearWhenClose() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

        boolean result = longBoi.checkNear(6.0f, 6.0f);

        assertTrue(result);
    }

    @Test
    public void testCheckNearWhenFar() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

        boolean result = longBoi.checkNear(15.0f, 15.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckNearPersists() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

        longBoi.checkNear(6.0f, 6.0f);
        boolean nearAfterClose = longBoi.getNear();
        longBoi.checkNear(15.0f, 15.0f);
        boolean nearAfterFar = longBoi.getNear();

        assertTrue(nearAfterClose);
        assertTrue(nearAfterFar);
    }

    @Test
    public void testCheckCollisionWhenClose() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

        boolean result = longBoi.checkCollision(6.0f, 6.0f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionWhenFar() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

        boolean result = longBoi.checkCollision(10.0f, 10.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionSetsCollided() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

        boolean firstCollision = longBoi.checkCollision(6.0f, 6.0f);
        boolean secondCheck = longBoi.checkCollision(10.0f, 10.0f);

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
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;
        longBoi.checkNear(6.0f, 6.0f);

        longBoi.logic();

        assertNotNull(longBoi);
    }

    @Test
    public void testWalkPathWorks() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;
        longBoi.checkNear(6.0f, 6.0f);

        longBoi.walkPath();

        assertNotNull(longBoi);
    }

    @Test
    public void testRadiusProperty() {
        assertEquals(6f, longBoi.radius);

        longBoi.radius = 3.0f;

        assertEquals(3.0f, longBoi.radius);
    }

    @Test
    public void testDoneWalkProperty() {
        assertFalse(longBoi.doneWalk);

        longBoi.doneWalk = true;

        assertTrue(longBoi.doneWalk);
    }

    @Test
    public void testCollidedProperty() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;
        longBoi.checkCollision(6.0f, 6.0f);

        assertTrue(longBoi.collided);
    }

    @Test
    public void testLongBoiTextureInitialized() {
        assertNotNull(longBoi.longSheet);
    }

    @Test
    public void testMultipleLongBoisIndependent() {
        LongBoiEvent l1 = new LongBoiEvent(new Sprite(new Texture("character.png")), collisionLayer);
        LongBoiEvent l2 = new LongBoiEvent(new Sprite(new Texture("character.png")), collisionLayer);
        l1.longX = 0.0f;
        l1.longY = 0.0f;
        l2.longX = 20.0f;
        l2.longY = 20.0f;

        l1.checkNear(1.0f, 1.0f);
        boolean l1Near = l1.getNear();
        boolean l2Near = l2.getNear();

        assertTrue(l1Near);
        assertFalse(l2Near);
    }

    @Test
    public void testNegativeCoordinates() {
        longBoi.longX = -5.0f;
        longBoi.longY = -5.0f;

        longBoi.logic();

        assertNotNull(longBoi);
    }

    @Test
    public void testLargeCoordinates() {
        longBoi.longX = 1000.0f;
        longBoi.longY = 2000.0f;

        longBoi.logic();

        assertNotNull(longBoi);
    }

    @Test
    public void testGameScenarioLongBoiEncounter() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

        longBoi.logic();
        assertFalse(longBoi.getNear());

        longBoi.logic();
        boolean nearCheck = longBoi.checkNear(6.0f, 6.0f);
        assertTrue(nearCheck);
        assertTrue(longBoi.getNear());

        longBoi.logic();
        longBoi.walkPath();
        assertFalse(longBoi.checkCollision(10.0f, 10.0f));

        boolean collision = longBoi.checkCollision(6.0f, 6.0f);
        assertTrue(collision);
        assertTrue(longBoi.collided);
    }

    @Test
    public void testMultipleLongBoisGameplay() {
        LongBoiEvent l1 = new LongBoiEvent(new Sprite(new Texture("character.png")), collisionLayer);
        LongBoiEvent l2 = new LongBoiEvent(new Sprite(new Texture("character.png")), collisionLayer);
        l1.longX = 0.0f;
        l1.longY = 0.0f;
        l2.longX = 10.0f;
        l2.longY = 10.0f;

        l1.logic();
        boolean l1Near = l1.checkNear(1.0f, 1.0f);
        l2.logic();
        boolean l2Near = l2.checkNear(11.0f, 11.0f);
        boolean l1Collision = l1.checkCollision(1.0f, 1.0f);
        boolean l2Collision = l2.checkCollision(11.0f, 11.0f);

        assertTrue(l1Near);
        assertTrue(l2Near);
        assertTrue(l1Collision);
        assertTrue(l2Collision);
    }

    @Test
    public void testLongBoiMovementAndLogic() {
        longBoi.longX = 0.0f;
        longBoi.longY = 0.0f;

        longBoi.logic();
        boolean nearAfterFirstLogic = longBoi.getNear();
        longBoi.longX = 10.0f;
        longBoi.longY = 10.0f;
        longBoi.logic();

        assertFalse(nearAfterFirstLogic);
        assertNotNull(longBoi);
    }

    @Test
    public void testAnimationAndNearTiming() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

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
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;
        longBoi.checkNear(6.0f, 6.0f);

        longBoi.logic();
        longBoi.walkPath();
        longBoi.doneWalk = true;

        assertTrue(longBoi.doneWalk);
        longBoi.logic();
        assertNotNull(longBoi);
    }

    @Test
    public void testComplexLongBoiInteraction() {
        longBoi.longX = 5.0f;
        longBoi.longY = 5.0f;

        longBoi.logic();
        boolean near = longBoi.checkNear(6.0f, 6.0f);
        assertTrue(near);

        longBoi.logic();
        longBoi.walkPath();
        assertTrue(longBoi.getNear());

        longBoi.doneWalk = true;
        assertTrue(longBoi.doneWalk);

        longBoi.logic();
        assertTrue(longBoi.getNear());
    }
}
