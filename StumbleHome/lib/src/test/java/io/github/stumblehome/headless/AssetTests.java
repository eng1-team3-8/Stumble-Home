package io.github.stumblehome.headless;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Gdx;
import io.github.stumblehome.LongBoiEvent;
import org.junit.jupiter.api.Test;

public class AssetTests extends AbstractHeadlessGdxTest {
    @Test
    public void testLongBoiTextureAtlasExists() {
        assertTrue(
                Gdx.files.internal(LongBoiEvent.ASSET).exists(),
                "LongBoi texture atlas should exist");
    }
}
