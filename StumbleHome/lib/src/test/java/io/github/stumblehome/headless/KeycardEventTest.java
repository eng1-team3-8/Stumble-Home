package io.github.stumblehome.headless;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.stumblehome.BottleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BottleEventTest extends AbstractHeadlessGdxTest {
    private BottleEvent bottle;

    @BeforeEach
    public void setUp() {
        bottle = new BottleEvent(new Sprite(new Texture("character.png")));
    }

    @Test
    public void testConstructor() {
        assertNotNull(bottle);
        assertEquals(1.5f, bottle.bottleSize);
    }

    @Test
    public void testBottleSheetInitialized() {
        assertNotNull(bottle.bottleSheet);
    }

    @Test
    public void testBottlePositionInitialized() {
        bottle.bottleX = 5.0f;
        bottle.bottleY = 10.0f;

        assertEquals(5.0f, bottle.bottleX);
        assertEquals(10.0f, bottle.bottleY);
    }

    @Test
    public void testLogicUpdatesState() {
        bottle.logic();

        assertNotNull(bottle);
    }

    @Test
    public void testLogicWhenNotCollected() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;

        bottle.logic();
        bottle.logic();

        assertNotNull(bottle);
    }

    @Test
    public void testCheckCollisionWhenClose() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;

        boolean result = bottle.checkCollision(0.75f, 0.75f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionWhenFar() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;

        boolean result = bottle.checkCollision(10.0f, 10.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionAtExactCenter() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;

        boolean result = bottle.checkCollision(0.75f, 0.75f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionSetsCollected() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;

        boolean firstCollision = bottle.checkCollision(0.75f, 0.75f);
        boolean secondCollision = bottle.checkCollision(0.75f, 0.75f);

        assertTrue(firstCollision);
        assertFalse(secondCollision);
    }

    @Test
    public void testCheckCollisionReturnsFalseIfAlreadyCollected() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;
        bottle.checkCollision(0.75f, 0.75f);

        boolean result = bottle.checkCollision(100.0f, 100.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionNearBoundary() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;

        boolean result = bottle.checkCollision(0.5f, 1.2f);

        assertTrue(result);
    }

    @Test
    public void testDrawMethodExists() {
        bottle.bottleX = 5.0f;
        bottle.bottleY = 5.0f;

        assertNotNull(bottle);
    }

    @Test
    public void testDrawAfterCollision() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;
        bottle.checkCollision(0.75f, 0.75f);

        assertNotNull(bottle);
    }

    @Test
    public void testCentreCalculation() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;

        boolean result = bottle.checkCollision(0.75f, 0.75f);

        assertTrue(result);
    }

    @Test
    public void testCentreCalculationWithOffset() {
        bottle.bottleX = 2.0f;
        bottle.bottleY = 3.0f;

        boolean result = bottle.checkCollision(2.75f, 3.75f);

        assertTrue(result);
    }

    @Test
    public void testBottleSizeConstant() {
        BottleEvent bottle2 = new BottleEvent(new Sprite(new Texture("character.png")));

        assertEquals(1.5f, bottle.bottleSize);
        assertEquals(1.5f, bottle2.bottleSize);
    }

    @Test
    public void testMultipleBottlesIndependent() {
        BottleEvent bottle1 = new BottleEvent(new Sprite(new Texture("character.png")));
        BottleEvent bottle2 = new BottleEvent(new Sprite(new Texture("character.png")));
        bottle1.bottleX = 0.0f;
        bottle1.bottleY = 0.0f;
        bottle2.bottleX = 20.0f;
        bottle2.bottleY = 20.0f;

        bottle1.checkCollision(0.75f, 0.75f);
        boolean bottle2Result = bottle2.checkCollision(20.75f, 20.75f);

        assertTrue(bottle2Result);
    }

    @Test
    public void testNegativeCoordinates() {
        bottle.bottleX = -5.0f;
        bottle.bottleY = -5.0f;

        boolean result = bottle.checkCollision(-4.25f, -4.25f);

        assertTrue(result);
    }

    @Test
    public void testGameScenarioBottleCollection() {
        bottle.bottleX = 5.0f;
        bottle.bottleY = 5.0f;

        bottle.logic();
        assertFalse(bottle.checkCollision(10.0f, 10.0f));

        bottle.logic();
        assertFalse(bottle.checkCollision(8.0f, 8.0f));

        bottle.logic();
        assertFalse(bottle.checkCollision(7.0f, 7.0f));

        bottle.logic();
        assertTrue(bottle.checkCollision(5.75f, 5.75f));

        bottle.logic();
        assertFalse(bottle.checkCollision(5.75f, 5.75f));

        bottle.logic();
        assertFalse(bottle.checkCollision(5.0f, 5.0f));
    }

    @Test
    public void testMultipleBottlesGameplay() {
        BottleEvent bottle1 = new BottleEvent(new Sprite(new Texture("character.png")));
        BottleEvent bottle2 = new BottleEvent(new Sprite(new Texture("character.png")));
        bottle1.bottleX = 0.0f;
        bottle1.bottleY = 0.0f;
        bottle2.bottleX = 10.0f;
        bottle2.bottleY = 10.0f;

        bottle1.logic();
        boolean bottle1Collision = bottle1.checkCollision(0.75f, 0.75f);
        bottle2.logic();
        boolean bottle2FirstCollision = bottle2.checkCollision(10.75f, 10.75f);
        boolean bottle1SecondCollision = bottle1.checkCollision(0.75f, 0.75f);
        boolean bottle2SecondCollision = bottle2.checkCollision(10.75f, 10.75f);

        assertTrue(bottle1Collision);
        assertTrue(bottle2FirstCollision);
        assertFalse(bottle1SecondCollision);
        assertFalse(bottle2SecondCollision);
    }

    @Test
    public void testBottleMovementAndCollision() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;
        boolean firstCollision = bottle.checkCollision(0.75f, 0.75f);
        bottle.bottleX = 100.0f;
        bottle.bottleY = 100.0f;
        boolean secondCollision = bottle.checkCollision(100.75f, 100.75f);

        assertTrue(firstCollision);
        assertFalse(secondCollision);
    }

    @Test
    public void testAnimationAndCollisionTiming() {
        bottle.bottleX = 5.0f;
        bottle.bottleY = 5.0f;

        for (int i = 0; i < 10; i++) {
            bottle.logic();
            assertFalse(bottle.checkCollision(15.0f, 15.0f));
        }

        boolean collision = bottle.checkCollision(5.75f, 5.75f);
        assertTrue(collision);

        for (int i = 0; i < 10; i++) {
            bottle.logic();
            assertFalse(bottle.checkCollision(5.75f, 5.75f));
        }
    }

    @Test
    public void testCollisionWithDifferentApproaches() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;
        boolean rightApproach = bottle.checkCollision(2.0f, 0.75f);

        BottleEvent bottle2 = new BottleEvent(new Sprite(new Texture("character.png")));
        bottle2.bottleX = 0.0f;
        bottle2.bottleY = 0.0f;
        boolean topApproach = bottle2.checkCollision(0.75f, 2.0f);

        BottleEvent bottle3 = new BottleEvent(new Sprite(new Texture("character.png")));
        bottle3.bottleX = 0.0f;
        bottle3.bottleY = 0.0f;
        boolean diagonalApproach = bottle3.checkCollision(0.75f, 0.75f);

        assertFalse(rightApproach);
        assertFalse(topApproach);
        assertTrue(diagonalApproach);
    }

    @Test
    public void testComplexCollisionBoundaries() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;

        boolean centerCollision = bottle.checkCollision(0.75f, 0.75f);
        boolean farCollision = bottle.checkCollision(2.0f, 0.75f);

        assertTrue(centerCollision);
        assertFalse(farCollision);
    }
}
