package io.github.stumblehome.headless;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;

public class MenuAssetTests extends AbstractHeadlessGdxTest {
    
    @Test
    public void testMainMenuAssets() {
        assertTrue(Gdx.files.internal("MainMenu.png").exists(), "Background image does not exist");
        assertTrue(Gdx.files.internal("tutorial.png").exists(), "Tutorial image does not exist");
        assertTrue(Gdx.files.internal("ui/uiskin.json").exists(), "UI skin does not exist");
    }

}
