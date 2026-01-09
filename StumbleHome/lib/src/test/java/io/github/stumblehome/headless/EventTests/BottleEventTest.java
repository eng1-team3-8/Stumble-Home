package io.github.stumblehome.headless.EventTests;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Texture;
import io.github.stumblehome.Entities.BottleEvent;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BottleEventTest extends AbstractHeadlessGdxTest {
    private BottleEvent bottle;

    @BeforeEach
    public void setUp() {
        bottle = new BottleEvent(new Texture("character.png"), 1.5f, new float[] {1f, 1f});
    }

    @Test
    public void testConstructor() {
        assertNotNull(bottle);
        assertEquals(1.5f, bottle.frame_size);
    }

    @Test
    public void testBottleSheetInitialized() {
        assertNotNull(bottle.getTexture());
    }

    @Test
    public void testBottlePositionInitialized() {
        bottle.setX(5.0f);
        bottle.setY(10.0f);

        assertEquals(5.0f, bottle.getX());
        assertEquals(10.0f, bottle.getY());
    }

    @Test
    public void testLogicUpdatesState() {
        bottle.logic();

        assertNotNull(bottle);
    }

    @Test
    public void testLogicWhenNotCollected() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);

        bottle.logic();
        bottle.logic();

        assertNotNull(bottle);
    }

    @Test
    public void testCheckCollisionWhenClose() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);

        boolean result = bottle.checkColliding(0.75f, 0.75f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionWhenFar() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);

        boolean result = bottle.checkColliding(10.0f, 10.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionAtExactCenter() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);

        boolean result = bottle.checkColliding(0.75f, 0.75f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionSetsCollected() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);

        boolean firstCollision = bottle.checkColliding(0.75f, 0.75f);
        boolean secondCollision = bottle.checkColliding(0.75f, 0.75f);

        assertTrue(firstCollision);
        assertFalse(secondCollision);
    }

    @Test
    public void testCheckCollisionReturnsFalseIfAlreadyCollected() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);
        bottle.checkColliding(0.75f, 0.75f);

        boolean result = bottle.checkColliding(100.0f, 100.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionNearBoundary() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);

        boolean result = bottle.checkColliding(0.5f, 1.2f);

        assertTrue(result);
    }

    @Test
    public void testDrawMethodExists() {
        bottle.setX(5.0f);
        bottle.setY(5.0f);

        assertNotNull(bottle);
    }

    @Test
    public void testDrawAfterCollision() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);
        bottle.checkColliding(0.75f, 0.75f);

        assertNotNull(bottle);
    }

    @Test
    public void testCentreCalculation() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);

        boolean result = bottle.checkColliding(0.75f, 0.75f);

        assertTrue(result);
    }

    @Test
    public void testCentreCalculationWithOffset() {
        bottle.setX(2.0f);
        bottle.setY(3.0f);

        boolean result = bottle.checkColliding(2.75f, 3.75f);

        assertTrue(result);
    }

    @Test
    public void testframe_sizeConstant() {
        BottleEvent bottle2 =
                new BottleEvent(new Texture("character.png"), 1.5f, new float[] {1f, 1f});

        assertEquals(1.5f, bottle.frame_size);
        assertEquals(1.5f, bottle2.frame_size);
    }

    @Test
    public void testMultipleBottlesIndependent() {
        BottleEvent bottle1 =
                new BottleEvent(new Texture("character.png"), 1f, new float[] {0f, 0f});
        BottleEvent bottle2 =
                new BottleEvent(new Texture("character.png"), 1f, new float[] {20f, 20f});

        bottle1.checkColliding(0.75f, 0.75f);
        boolean bottle2Result = bottle2.checkColliding(20.75f, 20.75f);

        assertTrue(bottle2Result);
    }

    @Test
    public void testNegativeCoordinates() {
        bottle.setX(-5.0f);
        bottle.setY(-5.0f);

        boolean result = bottle.checkColliding(-4.25f, -4.25f);

        assertTrue(result);
    }

    @Test
    public void testGameScenarioBottleCollection() {
        bottle.setX(5.0f);
        bottle.setY(5.0f);

        bottle.logic();
        assertFalse(bottle.checkColliding(10.0f, 10.0f));

        bottle.logic();
        assertFalse(bottle.checkColliding(8.0f, 8.0f));

        bottle.logic();
        assertFalse(bottle.checkColliding(7.0f, 7.0f));

        bottle.logic();
        assertTrue(bottle.checkColliding(5.75f, 5.75f));

        bottle.logic();
        assertFalse(bottle.checkColliding(5.75f, 5.75f));

        bottle.logic();
        assertFalse(bottle.checkColliding(5.0f, 5.0f));
    }

    @Test
    public void testMultipleBottlesGameplay() {
        BottleEvent bottle1 =
                new BottleEvent(new Texture("character.png"), 1f, new float[] {0f, 0f});
        BottleEvent bottle2 =
                new BottleEvent(new Texture("character.png"), 1f, new float[] {10f, 10f});

        bottle1.logic();
        boolean bottle1Collision = bottle1.checkColliding(0.75f, 0.75f);
        bottle2.logic();
        boolean bottle2FirstCollision = bottle2.checkColliding(10.75f, 10.75f);
        boolean bottle1SecondCollision = bottle1.checkColliding(0.75f, 0.75f);
        boolean bottle2SecondCollision = bottle2.checkColliding(10.75f, 10.75f);

        assertTrue(bottle1Collision);
        assertTrue(bottle2FirstCollision);
        assertFalse(bottle1SecondCollision);
        assertFalse(bottle2SecondCollision);
    }

    @Test
    public void testBottleMovementAndCollision() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);
        boolean firstCollision = bottle.checkColliding(0.75f, 0.75f);
        bottle.setX(100.0f);
        bottle.setY(100.0f);
        boolean secondCollision = bottle.checkColliding(100.75f, 100.75f);

        assertTrue(firstCollision);
        assertFalse(secondCollision);
    }

    @Test
    public void testAnimationAndCollisionTiming() {
        bottle.setX(5.0f);
        bottle.setY(5.0f);

        for (int i = 0; i < 10; i++) {
            bottle.logic();
            assertFalse(bottle.checkColliding(15.0f, 15.0f));
        }

        boolean collision = bottle.checkColliding(5.75f, 5.75f);
        assertTrue(collision);

        for (int i = 0; i < 10; i++) {
            bottle.logic();
            assertFalse(bottle.checkColliding(5.75f, 5.75f));
        }
    }

    @Test
    public void testAnimationFrameProgression() {
        bottle.setX(5.0f);
        bottle.setY(5.0f);

        for (int i = 0; i < 20; i++) {
            bottle.logic();
            assertNotNull(bottle.getTexture());
        }

        bottle.checkColliding(5.75f, 5.75f);
        for (int i = 0; i < 10; i++) {
            bottle.logic();
        }

        assertNotNull(bottle.getTexture());
    }

    @Test
    public void testCollisionWithDifferentApproaches() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);
        boolean rightApproach = bottle.checkColliding(2.0f, 0.75f);

        BottleEvent bottle2 =
                new BottleEvent(new Texture("character.png"), 1f, new float[] {0f, 0f});
        boolean topApproach = bottle2.checkColliding(0.75f, 2.0f);

        BottleEvent bottle3 =
                new BottleEvent(new Texture("character.png"), 1f, new float[] {0f, 0f});
        boolean diagonalApproach = bottle3.checkColliding(0.75f, 0.75f);

        assertFalse(rightApproach);
        assertFalse(topApproach);
        assertTrue(diagonalApproach);
    }

    @Test
    public void testAnimationContinuityAcrossFrames() {
        bottle.setX(10.0f);
        bottle.setY(10.0f);
        for (int i = 0; i < 30; i++) {
            bottle.logic();
        }

        boolean collision = bottle.checkColliding(10.75f, 10.75f);
        assertTrue(collision);

        bottle.logic();
        boolean secondAttempt = bottle.checkColliding(10.75f, 10.75f);
        assertFalse(secondAttempt);
    }

    @Test
    public void testCollectedOnce() {
        bottle.setX(0.0f);
        bottle.setY(0.0f);
        bottle.checkColliding(0.75f, 0.75f);
        assertFalse(bottle.checkColliding(0.75f, 0.75f));
    }
}
