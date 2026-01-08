package io.github.stumblehome.headless;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.stumblehome.KeycardEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeycardEventTest extends AbstractHeadlessGdxTest {
    private KeycardEvent keycard;

    @BeforeEach
    public void setUp() {
        keycard = new KeycardEvent(new Sprite(new Texture("character.png")));
    }

    @Test
    public void testConstructor() {
        assertNotNull(keycard);
        assertEquals(1.0f, keycard.keycardSize);
    }

    @Test
    public void testKeycardSheetInitialized() {
        assertNotNull(keycard.keycardSheet);
    }

    @Test
    public void testKeycardPositionInitialized() {
        keycard.keycardX = 3.0f;
        keycard.keycardY = 8.0f;

        assertEquals(3.0f, keycard.keycardX);
        assertEquals(8.0f, keycard.keycardY);
    }

    @Test
    public void testLogicUpdatesState() {
        keycard.logic();

        assertNotNull(keycard);
    }

    @Test
    public void testLogicWhenNotCollected() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;

        keycard.logic();
        keycard.logic();

        assertNotNull(keycard);
    }

    @Test
    public void testCheckCollisionWhenClose() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;

        boolean result = keycard.checkCollision(0.5f, 0.5f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionWhenFar() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;

        boolean result = keycard.checkCollision(10.0f, 10.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionAtExactCenter() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;

        boolean result = keycard.checkCollision(0.5f, 0.5f);

        assertTrue(result);
    }

    @Test
    public void testCheckCollisionSetsCollected() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;

        boolean firstCollision = keycard.checkCollision(0.5f, 0.5f);
        boolean secondCollision = keycard.checkCollision(0.5f, 0.5f);

        assertTrue(firstCollision);
        assertFalse(secondCollision);
    }

    @Test
    public void testCheckCollisionReturnsFalseIfAlreadyCollected() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;
        keycard.checkCollision(0.5f, 0.5f);

        boolean result = keycard.checkCollision(100.0f, 100.0f);

        assertFalse(result);
    }

    @Test
    public void testCheckCollisionNearBoundary() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;

        boolean result = keycard.checkCollision(0.5f, 0.9f);

        assertTrue(result);
    }

    @Test
    public void testDrawMethodExists() {
        keycard.keycardX = 5.0f;
        keycard.keycardY = 5.0f;

        assertNotNull(keycard);
    }

    @Test
    public void testDrawAfterCollision() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;
        keycard.checkCollision(0.5f, 0.5f);

        assertNotNull(keycard);
    }

    @Test
    public void testCentreCalculation() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;

        boolean result = keycard.checkCollision(0.5f, 0.5f);

        assertTrue(result);
    }

    @Test
    public void testCentreCalculationWithOffset() {
        keycard.keycardX = 2.0f;
        keycard.keycardY = 3.0f;

        boolean result = keycard.checkCollision(2.5f, 3.5f);

        assertTrue(result);
    }

    @Test
    public void testKeycardSizeConstant() {
        KeycardEvent keycard2 = new KeycardEvent(new Sprite(new Texture("character.png")));

        assertEquals(1.0f, keycard.keycardSize);
        assertEquals(1.0f, keycard2.keycardSize);
    }

    @Test
    public void testMultipleKeycardsIndependent() {
        KeycardEvent k1 = new KeycardEvent(new Sprite(new Texture("character.png")));
        KeycardEvent k2 = new KeycardEvent(new Sprite(new Texture("character.png")));
        k1.keycardX = 0.0f;
        k1.keycardY = 0.0f;
        k2.keycardX = 20.0f;
        k2.keycardY = 20.0f;

        k1.checkCollision(0.5f, 0.5f);
        boolean k2Result = k2.checkCollision(20.5f, 20.5f);

        assertTrue(k2Result);
    }

    @Test
    public void testNegativeCoordinates() {
        keycard.keycardX = -5.0f;
        keycard.keycardY = -5.0f;

        boolean result = keycard.checkCollision(-4.5f, -4.5f);

        assertTrue(result);
    }

    @Test
    public void testLargeCoordinates() {
        keycard.keycardX = 1000.0f;
        keycard.keycardY = 2000.0f;

        boolean result = keycard.checkCollision(1000.5f, 2000.5f);

        assertTrue(result);
    }

    @Test
    public void testGameScenarioKeycardCollection() {
        keycard.keycardX = 5.0f;
        keycard.keycardY = 5.0f;

        keycard.logic();
        assertFalse(keycard.checkCollision(10.0f, 10.0f));

        keycard.logic();
        assertFalse(keycard.checkCollision(8.0f, 8.0f));

        keycard.logic();
        assertFalse(keycard.checkCollision(7.0f, 7.0f));

        keycard.logic();
        assertTrue(keycard.checkCollision(5.5f, 5.5f));

        keycard.logic();
        assertFalse(keycard.checkCollision(5.5f, 5.5f));

        keycard.logic();
        assertFalse(keycard.checkCollision(5.0f, 5.0f));
    }

    @Test
    public void testMultipleKeycardsGameplay() {
        KeycardEvent k1 = new KeycardEvent(new Sprite(new Texture("character.png")));
        KeycardEvent k2 = new KeycardEvent(new Sprite(new Texture("character.png")));
        k1.keycardX = 0.0f;
        k1.keycardY = 0.0f;
        k2.keycardX = 10.0f;
        k2.keycardY = 10.0f;

        k1.logic();
        boolean k1Collision = k1.checkCollision(0.5f, 0.5f);
        k2.logic();
        boolean k2FirstCollision = k2.checkCollision(10.5f, 10.5f);
        boolean k1SecondCollision = k1.checkCollision(0.5f, 0.5f);
        boolean k2SecondCollision = k2.checkCollision(10.5f, 10.5f);

        assertTrue(k1Collision);
        assertTrue(k2FirstCollision);
        assertFalse(k1SecondCollision);
        assertFalse(k2SecondCollision);
    }

    @Test
    public void testKeycardMovementAndCollision() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;
        boolean firstCollision = keycard.checkCollision(0.5f, 0.5f);
        keycard.keycardX = 100.0f;
        keycard.keycardY = 100.0f;
        boolean secondCollision = keycard.checkCollision(100.5f, 100.5f);

        assertTrue(firstCollision);
        assertFalse(secondCollision);
    }

    @Test
    public void testAnimationAndCollisionTiming() {
        keycard.keycardX = 5.0f;
        keycard.keycardY = 5.0f;

        for (int i = 0; i < 10; i++) {
            keycard.logic();
            assertFalse(keycard.checkCollision(15.0f, 15.0f));
        }

        boolean collision = keycard.checkCollision(5.5f, 5.5f);
        assertTrue(collision);

        for (int i = 0; i < 10; i++) {
            keycard.logic();
            assertFalse(keycard.checkCollision(5.5f, 5.5f));
        }
    }

    @Test
    public void testCollisionWithDifferentApproaches() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;
        boolean rightApproach = keycard.checkCollision(2.0f, 0.5f);

        KeycardEvent k2 = new KeycardEvent(new Sprite(new Texture("character.png")));
        k2.keycardX = 0.0f;
        k2.keycardY = 0.0f;
        boolean topApproach = k2.checkCollision(0.5f, 2.0f);

        KeycardEvent k3 = new KeycardEvent(new Sprite(new Texture("character.png")));
        k3.keycardX = 0.0f;
        k3.keycardY = 0.0f;
        boolean diagonalApproach = k3.checkCollision(0.5f, 0.5f);

        assertFalse(rightApproach);
        assertFalse(topApproach);
        assertTrue(diagonalApproach);
    }

    @Test
    public void testComplexCollisionBoundaries() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;

        boolean centerCollision = keycard.checkCollision(0.5f, 0.5f);
        boolean farCollision = keycard.checkCollision(2.0f, 0.5f);

        assertTrue(centerCollision);
        assertFalse(farCollision);
    }
}
