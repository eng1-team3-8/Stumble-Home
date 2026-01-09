package io.github.stumblehome.headless.EventTests;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.stumblehome.Entities.*;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityInitializationTests extends AbstractHeadlessGdxTest {
    private Texture texture;
    private float testSize = 2.0f;
    private float[] testPosition = {10f, 20f};

    @BeforeEach
    public void setUp() {
        texture = new Texture(Gdx.files.internal("Sprites/Chicken.png"));
    }

    @Test
    public void testFoodInitializationPosition() {
        Food food = new Food(texture, testSize, testPosition);
        assertEquals(10f, food.getX());
        assertEquals(20f, food.getY());
    }

    @Test
    public void testFoodInitializationSize() {
        Food food = new Food(texture, testSize, testPosition);
        assertEquals(testSize, food.getWidth());
        assertEquals(testSize, food.getHeight());
    }

    @Test
    public void testFoodInitialNotCollected() {
        Food food = new Food(texture, testSize, testPosition);
        assertFalse(food.isCollected);
    }

    @Test
    public void testTwigInitializationPosition() {
        Texture twigTexture = new Texture(Gdx.files.internal("Sprites/Stick.png"));
        Twig twig = new Twig(twigTexture, testSize, testPosition);
        assertEquals(10f, twig.getX());
        assertEquals(20f, twig.getY());
    }

    @Test
    public void testTwigInitializationSize() {
        Texture twigTexture = new Texture(Gdx.files.internal("Sprites/Stick.png"));
        Twig twig = new Twig(twigTexture, testSize, testPosition);
        assertEquals(testSize, twig.getWidth());
        assertEquals(testSize, twig.getHeight());
    }

    @Test
    public void testTwigInitialNotCollected() {
        Texture twigTexture = new Texture(Gdx.files.internal("Sprites/Stick.png"));
        Twig twig = new Twig(twigTexture, testSize, testPosition);
        assertFalse(twig.isCollected);
    }

    @Test
    public void testChainsawInitializationPosition() {
        Texture chainsawTexture = new Texture(Gdx.files.internal("Sprites/Chainsaw.png"));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, testSize, testPosition);
        assertEquals(10f, chainsaw.getX());
        assertEquals(20f, chainsaw.getY());
    }

    @Test
    public void testChainsawInitializationSize() {
        Texture chainsawTexture = new Texture(Gdx.files.internal("Sprites/Chainsaw.png"));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, testSize, testPosition);
        assertEquals(testSize, chainsaw.getWidth());
        assertEquals(testSize, chainsaw.getHeight());
    }

    @Test
    public void testChainsawInitialNotCollected() {
        Texture chainsawTexture = new Texture(Gdx.files.internal("Sprites/Chainsaw.png"));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, testSize, testPosition);
        assertFalse(chainsaw.isCollected);
    }

    @Test
    public void testBottleEventInitializationPosition() {
        Texture bottleTexture = new Texture(Gdx.files.internal("waterBottle.png"));
        BottleEvent bottle = new BottleEvent(bottleTexture, testSize, testPosition);
        assertEquals(10f, bottle.getX());
        assertEquals(20f, bottle.getY());
    }

    @Test
    public void testBottleEventInitializationSize() {
        Texture bottleTexture = new Texture(Gdx.files.internal("waterBottle.png"));
        BottleEvent bottle = new BottleEvent(bottleTexture, testSize, testPosition);
        assertEquals(testSize, bottle.getWidth());
        assertEquals(testSize, bottle.getHeight());
    }

    @Test
    public void testBottleEventInitialNotCollected() {
        Texture bottleTexture = new Texture(Gdx.files.internal("waterBottle.png"));
        BottleEvent bottle = new BottleEvent(bottleTexture, testSize, testPosition);
        assertFalse(bottle.isCollected);
    }

    @Test
    public void testMultipleEntitiesIndependentState() {
        Food food = new Food(texture, testSize, testPosition);
        Texture twigTexture = new Texture(Gdx.files.internal("Sprites/Stick.png"));
        Twig twig = new Twig(twigTexture, testSize, new float[] {50f, 60f});

        assertFalse(food.isCollected);
        assertFalse(twig.isCollected);
        assertEquals(10f, food.getX());
        assertEquals(50f, twig.getX());
    }

    @Test
    public void testDifferentSizesInitialization() {
        float smallSize = 1.0f;
        float largeSize = 3.5f;
        Food smallFood = new Food(texture, smallSize, testPosition);
        Food largeFood = new Food(texture, largeSize, testPosition);

        assertEquals(smallSize, smallFood.getWidth());
        assertEquals(largeSize, largeFood.getWidth());
    }
}
