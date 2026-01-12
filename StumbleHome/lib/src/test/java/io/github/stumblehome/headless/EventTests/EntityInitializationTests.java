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
    private static final float TESTSIZE = 2.0f;
    private static final float SMALLSIZE = 1.0f;
    private static final float LARGESIZE = 3.5f;
    private static final float[] TESTPOSITION = {10f, 20f};
    private static final float[] ALTPOSITION = {5f, 5f};
    private static final float[] FARPOSITION = {50f, 60f};

    @BeforeEach
    public void setUp() {
        texture = new Texture(Gdx.files.internal(Food.ASSET));
    }

    @Test
    public void testAlcoholInitialization() {
        Texture alcoholTexture = new Texture(Gdx.files.internal(Alcohol.ASSET_TSING));
        Alcohol alcohol = new Alcohol(alcoholTexture, SMALLSIZE, ALTPOSITION);
        assertFalse(alcohol.isCollected);
        assertFalse(alcohol.isTriggered);
    }

    @Test
    public void testBottleEventInitialization() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, SMALLSIZE, ALTPOSITION);
        assertFalse(bottle.isCollected);
        assertFalse(bottle.isTriggered);
    }

    @Test
    public void testKeycardEventInitialization() {
        Texture keycardTexture = new Texture(Gdx.files.internal(KeycardEvent.ASSET));
        KeycardEvent keycard = new KeycardEvent(keycardTexture, SMALLSIZE, ALTPOSITION);
        assertFalse(keycard.isCollected);
        assertFalse(keycard.isTriggered);
    }

    @Test
    public void testFoodInitializationPosition() {
        Food food = new Food(texture, TESTSIZE, TESTPOSITION);
        assertEquals(10f, food.getX());
        assertEquals(20f, food.getY());
    }

    @Test
    public void testFoodInitializationSize() {
        Food food = new Food(texture, TESTSIZE, TESTPOSITION);
        assertEquals(TESTSIZE, food.getWidth());
        assertEquals(TESTSIZE, food.getHeight());
    }

    @Test
    public void testFoodInitialNotCollected() {
        Food food = new Food(texture, TESTSIZE, TESTPOSITION);
        assertFalse(food.isCollected);
    }

    @Test
    public void testTwigInitializationPosition() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, TESTSIZE, TESTPOSITION);
        assertEquals(10f, twig.getX());
        assertEquals(20f, twig.getY());
    }

    @Test
    public void testTwigInitializationSize() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, TESTSIZE, TESTPOSITION);
        assertEquals(TESTSIZE, twig.getWidth());
        assertEquals(TESTSIZE, twig.getHeight());
    }

    @Test
    public void testTwigInitialNotCollected() {
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, TESTSIZE, TESTPOSITION);
        assertFalse(twig.isCollected);
    }

    @Test
    public void testChainsawInitializationPosition() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, TESTSIZE, TESTPOSITION);
        assertEquals(10f, chainsaw.getX());
        assertEquals(20f, chainsaw.getY());
    }

    @Test
    public void testChainsawInitializationSize() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, TESTSIZE, TESTPOSITION);
        assertEquals(TESTSIZE, chainsaw.getWidth());
        assertEquals(TESTSIZE, chainsaw.getHeight());
    }

    @Test
    public void testChainsawInitialNotCollected() {
        Texture chainsawTexture = new Texture(Gdx.files.internal(Chainsaw.ASSET));
        Chainsaw chainsaw = new Chainsaw(chainsawTexture, TESTSIZE, TESTPOSITION);
        assertFalse(chainsaw.isCollected);
    }

    @Test
    public void testBottleEventInitializationPosition() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, TESTSIZE, TESTPOSITION);
        assertEquals(10f, bottle.getX());
        assertEquals(20f, bottle.getY());
    }

    @Test
    public void testBottleEventInitializationSize() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, TESTSIZE, TESTPOSITION);
        assertEquals(TESTSIZE, bottle.getWidth());
        assertEquals(TESTSIZE, bottle.getHeight());
    }

    @Test
    public void testBottleEventInitialNotCollected() {
        Texture bottleTexture = new Texture(Gdx.files.internal(BottleEvent.ASSET));
        BottleEvent bottle = new BottleEvent(bottleTexture, TESTSIZE, TESTPOSITION);
        assertFalse(bottle.isCollected);
    }

    @Test
    public void testMultipleEntitiesIndependentState() {
        Food food = new Food(texture, TESTSIZE, TESTPOSITION);
        Texture twigTexture = new Texture(Gdx.files.internal(Twig.ASSET));
        Twig twig = new Twig(twigTexture, TESTSIZE, FARPOSITION);
        assertFalse(food.isCollected);
        assertFalse(twig.isCollected);
        assertEquals(10f, food.getX());
        assertEquals(50f, twig.getX());
    }

    @Test
    public void testDifferentSizesInitialization() {
        Food smallFood = new Food(texture, SMALLSIZE, TESTPOSITION);
        Food largeFood = new Food(texture, LARGESIZE, TESTPOSITION);
        assertEquals(SMALLSIZE, smallFood.getWidth());
        assertEquals(LARGESIZE, largeFood.getWidth());
    }
}
