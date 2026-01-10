package io.github.stumblehome.headless.EventTests;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.stumblehome.Entities.*;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityStateTests extends AbstractHeadlessGdxTest {
    private Texture texture;
    private float testSize = 2.0f;
    private float[] testPosition = {10f, 20f};

    @BeforeEach
    public void setUp() {
        texture = new Texture(Gdx.files.internal(Food.ASSET));
    }

    @Test
    public void testFoodBoundsAreValid() {
        Food food = new Food(texture, testSize, testPosition);
        assertTrue(food.getWidth() > 0);
        assertTrue(food.getHeight() > 0);
    }

    @Test
    public void testFoodPositionBounds() {
        Food food = new Food(texture, testSize, testPosition);
        assertTrue(food.getX() >= 0);
        assertTrue(food.getY() >= 0);
    }

    @Test
    public void testTwigBoundsAreValid() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, testSize, testPosition);
        assertTrue(twig.getWidth() > 0);
        assertTrue(twig.getHeight() > 0);
    }

    @Test
    public void testTwigPositionBounds() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, testSize, testPosition);
        assertTrue(twig.getX() >= 0);
        assertTrue(twig.getY() >= 0);
    }

    @Test
    public void testChainsawBoundsAreValid() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, testSize, testPosition);
        assertTrue(chainsaw.getWidth() > 0);
        assertTrue(chainsaw.getHeight() > 0);
    }

    @Test
    public void testChainsawPositionBounds() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, testSize, testPosition);
        assertTrue(chainsaw.getX() >= 0);
        assertTrue(chainsaw.getY() >= 0);
    }

    @Test
    public void testBottleEventBoundsAreValid() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, testSize, testPosition);
        assertTrue(bottle.getWidth() > 0);
        assertTrue(bottle.getHeight() > 0);
    }

    @Test
    public void testBottleEventPositionBounds() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, testSize, testPosition);
        assertTrue(bottle.getX() >= 0);
        assertTrue(bottle.getY() >= 0);
    }

    @Test
    public void testBottleEventLogicProcessing() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, testSize, testPosition);
        bottle.logic();
        assertFalse(bottle.isCollected);
    }

    @Test
    public void testFoodStateTimeInitialized() {
        Food food = new Food(texture, testSize, testPosition);
        food.logic();
        assertFalse(food.isCollected);
    }

    @Test
    public void testEntitySizeConsistent() {
        float customSize = 3.5f;
        Food food = new Food(texture, customSize, testPosition);
        assertEquals(customSize, food.getWidth());
        assertEquals(customSize, food.getHeight());
    }

    @Test
    public void testNegativePositionAllowed() {
        float[] negativePosition = {-5f, -10f};
        Food food = new Food(texture, testSize, negativePosition);
        assertEquals(-5f, food.getX());
        assertEquals(-10f, food.getY());
    }

    @Test
    public void testLargePositionValues() {
        float[] largePosition = {1000f, 2000f};
        Food food = new Food(texture, testSize, largePosition);
        assertEquals(1000f, food.getX());
        assertEquals(2000f, food.getY());
    }

    @Test
    public void testZeroPositionInitialization() {
        float[] zeroPosition = {0f, 0f};
        Food food = new Food(texture, testSize, zeroPosition);
        assertEquals(0f, food.getX());
        assertEquals(0f, food.getY());
    }

    @Test
    public void testEntityDisposeDoesNotThrow() {
        Food food = new Food(texture, testSize, testPosition);
        assertDoesNotThrow(food::dispose);
    }

    @Test
    public void testEntityLogicDoesNotThrow() {
        Food food = new Food(texture, testSize, testPosition);
        assertDoesNotThrow(food::logic);
    }

    @Test
    public void testCollectableEntityCheckColliding() {
        Food food = new Food(texture, testSize, testPosition);
        float playerX = food.getX() + 0.5f;
        float playerY = food.getY() + 0.5f;
        boolean collided = food.checkColliding(playerX, playerY);
        assertTrue(collided);
        assertTrue(food.isCollected);
    }

    @Test
    public void testCollectableEntityCheckCollidingAfterCollected() {
        Food food = new Food(texture, testSize, testPosition);
        food.isCollected = true;
        boolean collided = food.checkColliding(food.getX(), food.getY());
        assertFalse(collided);
    }

    @Test
    public void testEntityFrameSizeInitialization() {
        Food food = new Food(texture, testSize, testPosition);
        assertEquals(testSize, food.frame_size);
    }

    @Test
    public void testEntityTextureNotNull() {
        Food food = new Food(texture, testSize, testPosition);
        assertNotNull(food.getTexture());
    }

    @Test
    public void testCollectableEntityIsTriggeredInitiallyFalse() {
        Food food = new Food(texture, testSize, testPosition);
        assertFalse(food.isTriggered);
    }

    @Test
    public void testCollectableEntityCheckCollidingFarAway() {
        Food food = new Food(texture, testSize, testPosition);
        boolean collided = food.checkColliding(1000f, 1000f);
        assertFalse(collided);
        assertFalse(food.isCollected);
    }

    @Test
    public void testCollectableEntityCheckCollidingExactPosition() {
        Food food = new Food(texture, testSize, testPosition);
        float centreX = food.getX() + food.frame_size / 2;
        float centreY = food.getY() + food.frame_size / 2;
        boolean collided = food.checkColliding(centreX, centreY);
        assertTrue(collided);
        assertTrue(food.isCollected);
    }

    @Test

    public void testMultipleCollectableEntitiesIndependentState() {
        Food food1 = new Food(texture, testSize, new float[]{10f, 10f});
        Food food2 = new Food(texture, testSize, new float[]{20f, 20f});
        food1.checkColliding(10.5f, 10.5f);
        assertTrue(food1.isCollected);
        assertFalse(food2.isCollected);
    }

    @Test
    public void testTwigCheckCollidingWorks() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, testSize, testPosition);
        float centreX = twig.getX() + twig.frame_size / 2;
        float centreY = twig.getY() + twig.frame_size / 2;
        boolean collided = twig.checkColliding(centreX, centreY);
        assertTrue(collided);
        assertTrue(twig.isCollected);
    }

    @Test
    public void testChainsawCheckCollidingWorks() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, testSize, testPosition);
        float centreX = chainsaw.getX() + chainsaw.frame_size / 2;
        float centreY = chainsaw.getY() + chainsaw.frame_size / 2;
        boolean collided = chainsaw.checkColliding(centreX, centreY);
        assertTrue(collided);
        assertTrue(chainsaw.isCollected);
    }

    @Test
    public void testBottleEventCheckCollidingWorks() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, testSize, testPosition);
        float centreX = bottle.getX() + bottle.frame_size / 2;
        float centreY = bottle.getY() + bottle.frame_size / 2;
        boolean collided = bottle.checkColliding(centreX, centreY);
        assertTrue(collided);
        assertTrue(bottle.isCollected);
    }
}
