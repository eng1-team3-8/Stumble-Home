package io.github.stumblehome.headless.ScreenTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Gdx;

import io.github.stumblehome.headless.AbstractHeadlessGdxTest;

import org.junit.jupiter.api.Test;

public class MenuAssetTests extends AbstractHeadlessGdxTest {

    @Test
    public void testMenuAssets() {
        assertTrue(Gdx.files.internal("MainMenu.png").exists(), "Background image does not exist");
        assertTrue(Gdx.files.internal("ui/uiskin.json").exists(), "UI skin does not exist");
    }

    @Test
    public void testMainMenuAssets() {
        assertTrue(Gdx.files.internal("tutorial.png").exists(), "Tutorial image does not exist");
    }

    @Test
    public void testLeaderboardAssets() {
        // assertTrue(
        //       Gdx.files.internal("leaderBoard.csv").exists(), "Leaderboard csv does not exist");
    }

    @Test
    public void testGameFinishAssets() {
        // Currently no custom assets for the end game screens.
        // this test exists for future development
    }
}
