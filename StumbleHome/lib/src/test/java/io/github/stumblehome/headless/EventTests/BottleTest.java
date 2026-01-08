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
    public void testBottleCreated() {
        assertNotNull(bottle);
    }

    @Test
    public void testCollision() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;
        assertTrue(bottle.checkCollision(0.75f, 0.75f));
    }

    @Test
    public void testCollectedOnce() {
        bottle.bottleX = 0.0f;
        bottle.bottleY = 0.0f;
        bottle.checkCollision(0.75f, 0.75f);
        assertFalse(bottle.checkCollision(0.75f, 0.75f));
    }
}
