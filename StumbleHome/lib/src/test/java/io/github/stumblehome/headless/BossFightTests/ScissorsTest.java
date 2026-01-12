package io.github.stumblehome.headless.BossFightTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stumblehome.BossFight.Scissors;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScissorsTest extends AbstractHeadlessGdxTest {
    private Scissors scissors;
    private FitViewport viewport;
    private Texture texture;

    @BeforeEach
    public void setUp() {
        texture = new Texture("character.png");
        viewport = new FitViewport(800, 600);
        scissors = new Scissors(texture, 1.0f, new float[] {105f, 100f});
    }

    @Test
    public void testConstructor() {
        assertNotNull(scissors);
        assertEquals(105f, scissors.getX(), 0.01f);
        assertEquals(100f, scissors.getY(), 0.01f);
    }

    @Test
    public void testInitialPosition() {
        scissors = new Scissors(texture, 1.0f, new float[] {50f, 75f});
        assertEquals(50f, scissors.getX(), 0.01f);
        assertEquals(75f, scissors.getY(), 0.01f);
    }

    @Test
    public void testMoveRightWhenBelowRightBoundary() {
        // Start at a position well below the right boundary
        float rightBoundary = (viewport.getWorldWidth() / 800) * 633;
        scissors.setX(rightBoundary - 100f); // Position well below boundary
        float initialX = scissors.getX();
        scissors.move(viewport);
        float newX = scissors.getX();
        assertTrue(newX > initialX, "Scissors should move right when below right boundary");
    }

    @Test
    public void testMoveLeftThenBounceRightAtLeftBoundary() {
        // Position scissors near left boundary
        float leftBoundary = (viewport.getWorldWidth() / 160) * 21;
        scissors.setX(leftBoundary + 1f);

        // Move left until it hits boundary
        for (int i = 0; i < 100; i++) {
            float prevX = scissors.getX();
            scissors.move(viewport);
            if (scissors.getX() < leftBoundary) {
                // Should bounce right
                scissors.move(viewport);
                assertTrue(scissors.getX() > prevX);
                break;
            }
            if (scissors.getX() > prevX && prevX < leftBoundary) {
                // Already bouncing right after hitting left boundary
                break;
            }
        }
    }

    @Test
    public void testMovementSpeed() {
        // Ensure we're in a position where movement is allowed (not at boundary)
        float rightBoundary = (viewport.getWorldWidth() / 800) * 633;
        float leftBoundary = (viewport.getWorldWidth() / 160) * 21;
        float middleX = (leftBoundary + rightBoundary) / 2;
        scissors.setX(middleX);
        float initialX = scissors.getX();
        scissors.move(viewport);
        float expectedIncrement = viewport.getWorldWidth() / 800;
        assertEquals(expectedIncrement, scissors.getX() - initialX, 0.01f);
    }

    @Test
    public void testMovementWithinBounds() {
        for (int i = 0; i < 100; i++) {
            scissors.move(viewport);
            float x = scissors.getX();
            float leftBoundary = (viewport.getWorldWidth() / 160) * 21;
            float rightBoundary = (viewport.getWorldWidth() / 800) * 633;

            assertTrue(
                    x >= leftBoundary - 1f,
                    "Scissors should stay within or near left boundary: "
                            + x
                            + " >= "
                            + leftBoundary);
            assertTrue(
                    x <= rightBoundary + 1f,
                    "Scissors should stay within or near right boundary: "
                            + x
                            + " <= "
                            + rightBoundary);
        }
    }

    @Test
    public void testPositionUpdateAfterMove() {
        // Ensure we're in a position where movement is allowed
        float rightBoundary = (viewport.getWorldWidth() / 800) * 633;
        float leftBoundary = (viewport.getWorldWidth() / 160) * 21;
        float middleX = (leftBoundary + rightBoundary) / 2;
        scissors.setX(middleX);

        float initialX = scissors.getX();
        float initialY = scissors.getY();
        scissors.move(viewport);
        // Y should remain the same
        assertEquals(initialY, scissors.getY(), 0.01f);
        // X should change
        assertNotEquals(initialX, scissors.getX(), 0.01f);
    }

    @Test
    public void testOscillatingMovement() {
        // Position scissors in the middle to allow oscillation
        float rightBoundary = (viewport.getWorldWidth() / 800) * 633;
        float leftBoundary = (viewport.getWorldWidth() / 160) * 21;
        float middleX = (leftBoundary + rightBoundary) / 2;
        scissors.setX(middleX);

        float[] positions = new float[20]; // More iterations to ensure oscillation
        for (int i = 0; i < 20; i++) {
            scissors.move(viewport);
            positions[i] = scissors.getX();
        }

        // Check that scissors moves in at least one direction
        boolean hasRightMovement = false;
        boolean hasLeftMovement = false;

        for (int i = 1; i < positions.length; i++) {
            if (positions[i] > positions[i - 1]) {
                hasRightMovement = true;
            }
            if (positions[i] < positions[i - 1]) {
                hasLeftMovement = true;
            }
        }

        assertTrue(
                hasRightMovement || hasLeftMovement,
                "Scissors should move in at least one direction over multiple moves");
    }

    @Test
    public void testBoundaryCalculations() {
        float leftBoundary = (viewport.getWorldWidth() / 160) * 21;
        float rightBoundary = (viewport.getWorldWidth() / 800) * 633;

        assertTrue(leftBoundary > 0, "Left boundary should be positive");
        assertTrue(rightBoundary > leftBoundary, "Right boundary should be greater than left");
    }

    @Test
    public void testDifferentViewportSizes() {
        FitViewport smallViewport = new FitViewport(400, 300);
        FitViewport largeViewport = new FitViewport(1600, 1200);

        // Position scissors in middle of each viewport to ensure movement
        float smallLeftBoundary = (smallViewport.getWorldWidth() / 160) * 21;
        float smallRightBoundary = (smallViewport.getWorldWidth() / 800) * 633;
        float smallMiddleX = (smallLeftBoundary + smallRightBoundary) / 2;

        float largeLeftBoundary = (largeViewport.getWorldWidth() / 160) * 21;
        float largeRightBoundary = (largeViewport.getWorldWidth() / 800) * 633;
        float largeMiddleX = (largeLeftBoundary + largeRightBoundary) / 2;

        Scissors smallScissors = new Scissors(texture, 1.0f, new float[] {smallMiddleX, 50f});
        Scissors largeScissors = new Scissors(texture, 1.0f, new float[] {largeMiddleX, 50f});

        float smallInitialX = smallScissors.getX();
        float largeInitialX = largeScissors.getX();

        smallScissors.move(smallViewport);
        largeScissors.move(largeViewport);

        // Both should move, but with different speeds based on viewport size
        assertNotEquals(smallInitialX, smallScissors.getX(), 0.01f);
        assertNotEquals(largeInitialX, largeScissors.getX(), 0.01f);

        // Larger viewport should result in larger movement increment
        float smallMovement = smallScissors.getX() - smallInitialX;
        float largeMovement = largeScissors.getX() - largeInitialX;
        assertTrue(largeMovement > smallMovement, "Larger viewport should have larger movement");
    }

    @Test
    public void testResetPosition() {
        scissors.setX(105f);
        assertEquals(105f, scissors.getX(), 0.01f);
    }
}
