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
    private static final float TEST_SIZE = 2.0f;
    private static final float SMALL_SIZE = 1.0f;
    private static final float LARGE_SIZE = 3.5f;
    private static final float[] TEST_POSITION = {10f, 20f};
    private static final float[] ALT_POSITION = {5f, 5f};
    private static final float[] FAR_POSITION = {50f, 60f};

    @BeforeEach
    public void setUp() {
        texture = new Texture(Gdx.files.internal(Food.ASSET));
    }

    @Test
    public void testAlcoholInitialization() {
        Texture alcoholTexture = new Texture(Gdx.files.internal(Alcohol.ASSET_TSING));
        Alcohol alcohol = new Alcohol(alcoholTexture, SMALL_SIZE, ALT_POSITION);
        assertFalse(alcohol.isCollected);
        assertFalse(alcohol.isTriggered);
    }

    @Test
    public void testBottleEventInitialization() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, SMALL_SIZE, ALT_POSITION);
        assertFalse(bottle.isCollected);
        assertFalse(bottle.isTriggered);
    }

    @Test
    public void testKeycardEventInitialization() {
        Texture keycardTexture = new Texture(Gdx.files.internal(KeycardEvent.ASSET));
        KeycardEvent keycard = new KeycardEvent(keycardTexture, SMALL_SIZE, ALT_POSITION);
        assertFalse(keycard.isCollected);
        assertFalse(keycard.isTriggered);
    }

    @Test
    public void testFoodInitializationPosition() {
        Food food = new Food(texture, TEST_SIZE, TEST_POSITION);
        assertEquals(10f, food.getX());
        assertEquals(20f, food.getY());
    }

    @Test
    public void testFoodInitializationSize() {
        Food food = new Food(texture, TEST_SIZE, TEST_POSITION);
        assertEquals(TEST_SIZE, food.getWidth());
        assertEquals(TEST_SIZE, food.getHeight());
    }

    @Test
    public void testFoodInitialNotCollected() {
        Food food = new Food(texture, TEST_SIZE, TEST_POSITION);
        assertFalse(food.isCollected);
    }

    @Test
    public void testTwigInitializationPosition() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, TEST_SIZE, TEST_POSITION);
        assertEquals(10f, twig.getX());
        assertEquals(20f, twig.getY());
    }

    @Test
    public void testTwigInitializationSize() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, TEST_SIZE, TEST_POSITION);
        assertEquals(TEST_SIZE, twig.getWidth());
        assertEquals(TEST_SIZE, twig.getHeight());
    }

    @Test
    public void testTwigInitialNotCollected() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, TEST_SIZE, TEST_POSITION);
        assertFalse(twig.isCollected);
    }

    @Test
    public void testChainsawInitializationPosition() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, TEST_SIZE, TEST_POSITION);
        assertEquals(10f, chainsaw.getX());
        assertEquals(20f, chainsaw.getY());
    }

    @Test
    public void testChainsawInitializationSize() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, TEST_SIZE, TEST_POSITION);
        assertEquals(TEST_SIZE, chainsaw.getWidth());
        assertEquals(TEST_SIZE, chainsaw.getHeight());
    }

    @Test
    public void testChainsawInitialNotCollected() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, TEST_SIZE, TEST_POSITION);
        assertFalse(chainsaw.isCollected);
    }

    @Test
    public void testBottleEventInitializationPosition() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, TEST_SIZE, TEST_POSITION);
        assertEquals(10f, bottle.getX());
        assertEquals(20f, bottle.getY());
    }

    @Test
    public void testBottleEventInitializationSize() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, TEST_SIZE, TEST_POSITION);
        assertEquals(TEST_SIZE, bottle.getWidth());
        assertEquals(TEST_SIZE, bottle.getHeight());
    }

    @Test
    public void testBottleEventInitialNotCollected() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, TEST_SIZE, TEST_POSITION);
        assertFalse(bottle.isCollected);
    }

    @Test
    public void testMultipleEntitiesIndependentState() {
        Food food = new Food(texture, TEST_SIZE, TEST_POSITION);
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, TEST_SIZE, FAR_POSITION);
        assertFalse(food.isCollected);
        assertFalse(twig.isCollected);
        assertEquals(10f, food.getX());
        assertEquals(50f, twig.getX());
    }

    @Test
    public void testDifferentSizesInitialization() {
        Food smallFood = new Food(texture, SMALL_SIZE, TEST_POSITION);
        Food largeFood = new Food(texture, LARGE_SIZE, TEST_POSITION);
        assertEquals(SMALL_SIZE, smallFood.getWidth());
        assertEquals(LARGE_SIZE, largeFood.getWidth());
    }
}
