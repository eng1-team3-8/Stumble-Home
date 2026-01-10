package io.github.stumblehome.headless.EventTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Gdx;
import io.github.stumblehome.Entities.Alcohol;
import io.github.stumblehome.Entities.Bob;
import io.github.stumblehome.Entities.Chainsaw;
import io.github.stumblehome.Entities.Food;
import io.github.stumblehome.Entities.Rose;
import io.github.stumblehome.Entities.Twig;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.Test;

public class AssetTests extends AbstractHeadlessGdxTest {
    @Test
    public void testFoodSpriteExists() {
        assertTrue(Gdx.files.internal(Food.ASSET).exists());
    }

    @Test
    public void testTwigSpriteExists() {
        assertTrue(Gdx.files.internal(Twig.ASSET).exists());
    }

    @Test
    public void testChainsawSpriteExists() {
        assertTrue(Gdx.files.internal(Chainsaw.ASSET).exists());
    }

    @Test
    public void testBobSpriteExists() {
        assertTrue(Gdx.files.internal(Bob.ASSET).exists());
    }

    @Test
    public void testRoseYorkSpriteExists() {
        assertTrue(Gdx.files.internal(Rose.ASSET_YORK).exists());
    }

    @Test
    public void testRoseLancasterSpriteExists() {
        assertTrue(Gdx.files.internal(Rose.ASSET_LANC).exists());
    }

    @Test
    public void testAlcoholTsingSprite() {
        assertTrue(Gdx.files.internal(Alcohol.ASSET_TSING).exists());
    }

    @Test
    public void testAlcoholSmirnovSprite() {
        assertTrue(Gdx.files.internal(Alcohol.ASSET_SMIRN).exists());
    }
}
