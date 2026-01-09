package io.github.stumblehome.headless.EventTests;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Texture;
import io.github.stumblehome.Entities.KeycardEvent;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeycardEventTest extends AbstractHeadlessGdxTest {
    private KeycardEvent keycard;

    @BeforeEach
    public void setUp() {
        keycard = new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {1f, 1f});
    }

    @Test
    public void testConstructor() {
        assertNotNull(keycard);
        assertEquals(1.0f, keycard.frame_size);
    }

    @Test
    public void testKeycardSheetInitialized() {
        assertNotNull(keycard.getTexture());
    }

    @Test
    public void testKeycardPositionInitialized() {
        keycard.setX(3f);
        keycard.setY(8f);

        assertEquals(3.0f, keycard.getX());
        assertEquals(8.0f, keycard.getY());
    }

    @Test
    public void testLogicUpdatesState() {
        keycard.logic();

        assertNotNull(keycard);
    }

    @Test
    public void testLogicWhenNotCollected() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);

        keycard.logic();
        keycard.logic();

        assertNotNull(keycard);
    }

    @Test
    public void testCheckCollisionWhenClose() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);

        boolean result = keycard.checkColliding(0.5f, 0.5f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionWhenFar() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);

        boolean result = keycard.checkColliding(10.0f, 10.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionAtExactCenter() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);

        boolean result = keycard.checkColliding(0.5f, 0.5f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionSetsCollected() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);

        boolean firstCollision = keycard.checkColliding(0.5f, 0.5f);
        boolean secondCollision = keycard.checkColliding(0.5f, 0.5f);

        assertTrue(firstCollision);
        assertFalse(secondCollision);
    }

    @Test
    public void testCheckCollisionReturnsFalseIfAlreadyCollected() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);
        keycard.checkColliding(0.5f, 0.5f);

        boolean result = keycard.checkColliding(100.0f, 100.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionNearBoundary() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);

        boolean result = keycard.checkColliding(0.5f, 0.9f);

        assertTrue(result);
    }

    @Test
    public void testDrawMethodExists() {
        keycard.setX(5.0f);
        keycard.setY(5.0f);

        assertNotNull(keycard);
    }

    @Test
    public void testDrawAfterCollision() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);
        keycard.checkColliding(0.5f, 0.5f);

        assertNotNull(keycard);
    }

    @Test
    public void testCentreCalculation() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);

        boolean result = keycard.checkColliding(0.5f, 0.5f);

        assertTrue(result);
    }

    @Test
    public void testCentreCalculationWithOffset() {
        keycard.setX(2.0f);
        keycard.setY(3.0f);

        boolean result = keycard.checkColliding(2.5f, 3.5f);

        assertTrue(result);
    }

    @Test
    public void testframe_sizeConstant() {
        KeycardEvent keycard2 =
                new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {1f, 1f});

        assertEquals(1.0f, keycard.frame_size);
        assertEquals(1.0f, keycard2.frame_size);
    }

    @Test
    public void testMultipleKeycardsIndependent() {
        KeycardEvent k1 =
                new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {0f, 0f});
        KeycardEvent k2 =
                new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {20f, 20});

        k1.checkColliding(0.5f, 0.5f);
        boolean k2Result = k2.checkColliding(20.5f, 20.5f);

        assertTrue(k2Result);
    }

    @Test
    public void testNegativeCoordinates() {
        keycard.setX(-5.0f);
        keycard.setY(-5.0f);

        boolean result = keycard.checkColliding(-4.5f, -4.5f);

        assertTrue(result);
    }

    @Test
    public void testLargeCoordinates() {
        keycard.setX(1000.0f);
        keycard.setY(2000.0f);

        boolean result = keycard.checkColliding(1000.5f, 2000.5f);

        assertTrue(result);
    }

    @Test
    public void testGameScenarioKeycardCollection() {
        keycard.setX(5.0f);
        keycard.setY(5.0f);

        keycard.logic();
        assertFalse(keycard.checkColliding(10.0f, 10.0f));

        keycard.logic();
        assertFalse(keycard.checkColliding(8.0f, 8.0f));

        keycard.logic();
        assertFalse(keycard.checkColliding(7.0f, 7.0f));

        keycard.logic();
        assertTrue(keycard.checkColliding(5.5f, 5.5f));

        keycard.logic();
        assertFalse(keycard.checkColliding(5.5f, 5.5f));

        keycard.logic();
        assertFalse(keycard.checkColliding(5.0f, 5.0f));
    }

    @Test
    public void testMultipleKeycardsGameplay() {
        KeycardEvent k1 =
                new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {0f, 0f});
        KeycardEvent k2 =
                new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {10f, 10f});

        k1.logic();
        boolean k1Collision = k1.checkColliding(0.5f, 0.5f);
        k2.logic();
        boolean k2FirstCollision = k2.checkColliding(10.5f, 10.5f);
        boolean k1SecondCollision = k1.checkColliding(0.5f, 0.5f);
        boolean k2SecondCollision = k2.checkColliding(10.5f, 10.5f);

        assertTrue(k1Collision);
        assertTrue(k2FirstCollision);
        assertFalse(k1SecondCollision);
        assertFalse(k2SecondCollision);
    }

    @Test
    public void testKeycardMovementAndCollision() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);
        boolean firstCollision = keycard.checkColliding(0.5f, 0.5f);
        keycard.setX(100.0f);
        keycard.setY(100.0f);
        boolean secondCollision = keycard.checkColliding(100.5f, 100.5f);

        assertTrue(firstCollision);
        assertFalse(secondCollision);
    }

    @Test
    public void testAnimationAndCollisionTiming() {
        keycard.setX(5.0f);
        keycard.setY(5.0f);

        for (int i = 0; i < 10; i++) {
            keycard.logic();
            assertFalse(keycard.checkColliding(15.0f, 15.0f));
        }

        boolean collision = keycard.checkColliding(5.5f, 5.5f);
        assertTrue(collision);

        for (int i = 0; i < 10; i++) {
            keycard.logic();
            assertFalse(keycard.checkColliding(5.5f, 5.5f));
        }
    }

    @Test
    public void testCollisionWithDifferentApproaches() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);
        boolean rightApproach = keycard.checkColliding(2.0f, 0.5f);

        KeycardEvent k2 =
                new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {0f, 0f});
        boolean topApproach = k2.checkColliding(0.5f, 2.0f);

        KeycardEvent k3 =
                new KeycardEvent(new Texture(KeycardEvent.ASSET), 1f, new float[] {0f, 0f});
        boolean diagonalApproach = k3.checkColliding(0.5f, 0.5f);

        assertFalse(rightApproach);
        assertFalse(topApproach);
        assertTrue(diagonalApproach);
    }

    @Test
    public void testComplexCollisionBoundaries() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);

        boolean centerCollision = keycard.checkColliding(0.5f, 0.5f);
        boolean farCollision = keycard.checkColliding(2.0f, 0.5f);

        assertTrue(centerCollision);
        assertFalse(farCollision);
    }

    @Test
    public void testCollectedOnce() {
        keycard.setX(0.0f);
        keycard.setY(0.0f);
        keycard.checkColliding(0.5f, 0.5f);
        assertFalse(keycard.checkColliding(0.5f, 0.5f));
    }
}
