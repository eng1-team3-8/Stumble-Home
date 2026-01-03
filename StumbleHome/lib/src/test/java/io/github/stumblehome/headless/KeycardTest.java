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
    public void testKeycardCreated() {
        assertNotNull(keycard);
    }

    @Test
    public void testCollision() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;
        assertTrue(keycard.checkCollision(0.5f, 0.5f));
    }

    @Test
    public void testCollectedOnce() {
        keycard.keycardX = 0.0f;
        keycard.keycardY = 0.0f;
        keycard.checkCollision(0.5f, 0.5f);
        assertFalse(keycard.checkCollision(0.5f, 0.5f));
    }
}

