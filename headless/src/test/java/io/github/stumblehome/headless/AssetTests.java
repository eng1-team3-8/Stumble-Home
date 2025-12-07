package io.github.stumblehome.headless;

import com.badlogic.gdx.Gdx;
// Change for test
import io.github.some_example_name.longBoiEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssetTests extends AbstractHeadlessGdxTest {
    @Test
    public void testLongBoiTextureAtlasExists() {
        new longBoiEvent();
        assertTrue(Gdx.files.internal(longBoiEvent.ASSET).exists(), "LongBoi texture atlas should exist");
    }
}
