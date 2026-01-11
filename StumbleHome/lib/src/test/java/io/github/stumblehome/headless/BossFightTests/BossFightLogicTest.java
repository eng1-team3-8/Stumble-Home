package io.github.stumblehome.headless.BossFightTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stumblehome.BossFight.BossFightLogic;
import io.github.stumblehome.BossFight.BossFightStatesManager;
import io.github.stumblehome.BossFight.Scissors;
import io.github.stumblehome.Entities.BossFightEntity;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BossFightLogicTest extends AbstractHeadlessGdxTest {
    private BossFightLogic bossFightLogic;
    private Texture cutCableTexture;
    private BossFightStatesManager statesManager;
    private FitViewport viewport;

    @BeforeEach
    public void setUp() {
        cutCableTexture = new Texture("character.png");
        statesManager = new BossFightStatesManager();
        viewport = new FitViewport(800, 600);
        bossFightLogic = new BossFightLogic(cutCableTexture, statesManager, viewport);
    }

    @Test
    public void testConstructor() {
        assertNotNull(bossFightLogic);
        assertEquals(100, bossFightLogic.getPlayerHealth());
        assertEquals(100, bossFightLogic.getMikeHealth());
        assertFalse(bossFightLogic.isFinalStage());
        assertFalse(bossFightLogic.checkIfWon());
    }

    @Test
    public void testAttackMikeReducesHealth() {
        int initialHealth = bossFightLogic.getMikeHealth();
        bossFightLogic.attackMike();
        int newHealth = bossFightLogic.getMikeHealth();
        assertTrue(newHealth < initialHealth, "Mike's health should decrease after attack");
        assertTrue(newHealth >= 0, "Mike's health should not be negative");
    }

    // Test commit
    @Test
    public void testMikeAttacksPlayer() {
        int initialHealth = bossFightLogic.getPlayerHealth();
        bossFightLogic.mikeAttacks();
        int newHealth = bossFightLogic.getPlayerHealth();
        assertTrue(
                newHealth < initialHealth, "Player's health should decrease after Mike's attack");
    }

    @Test
    public void testWinConditionWhenMikeDefeated() {
        assertFalse(bossFightLogic.checkIfWon());
        // Attack until Mike's health reaches 0
        while (bossFightLogic.getMikeHealth() > 0) {
            bossFightLogic.attackMike();
        }
        assertEquals(0, bossFightLogic.getMikeHealth());
        assertTrue(bossFightLogic.checkIfWon(), "Player should win when Mike's health reaches 0");
    }

    @Test
    public void testMikeAttacksDoNotAffectPlayerIfAlreadyWon() {
        // Win the game first
        while (bossFightLogic.getMikeHealth() > 0) {
            bossFightLogic.attackMike();
        }
        assertTrue(bossFightLogic.checkIfWon());

        int healthAfterWin = bossFightLogic.getPlayerHealth();
        bossFightLogic.mikeAttacks();
        assertEquals(
                healthAfterWin,
                bossFightLogic.getPlayerHealth(),
                "Mike should not attack if player already won");
    }

    @Test
    public void testCheckOverlapWithOverlappingCable() throws InterruptedException {
        BossFightEntity[] cables = new BossFightEntity[4];
        Texture cableTexture = new Texture("character.png");
        float overlapThreshold = (viewport.getWorldWidth() / 80) * 3;
        float cableX = 100f;

        // Position cables far apart to avoid accidental overlaps
        for (int i = 0; i < cables.length; i++) {
            cables[i] =
                    new BossFightEntity(
                            cableTexture,
                            1.0f,
                            new float[] {cableX + i * (overlapThreshold * 3), 200f});
        }

        Scissors scissors =
                new Scissors(new Texture("character.png"), 1.0f, new float[] {100f, 100f});
        scissors.setX(cableX); // Position scissors at exact cable position

        Texture originalTexture = cables[0].getTexture();
        assertNotNull(originalTexture);
        assertNotNull(cutCableTexture);

        // Before overlap check, cable should have original texture
        assertEquals(originalTexture, cables[0].getTexture());

        bossFightLogic.checkOverlap(cables, scissors);

        // Verify that the cable texture was changed (cable was cut)
        assertEquals(cutCableTexture, cables[0].getTexture());
        assertNotEquals(originalTexture, cables[0].getTexture());
    }

    @Test
    public void testCutAllCablesTriggersFinalStage() throws InterruptedException {
        BossFightEntity[] cables = new BossFightEntity[4];
        Texture cableTexture = new Texture("character.png");
        float overlapThreshold = (viewport.getWorldWidth() / 80) * 3;
        float baseX = 100f;
        // Space cables far apart (threshold * 3 = 90 units for viewport 800)
        float[] cablePositions = {
            baseX,
            baseX + overlapThreshold * 3,
            baseX + overlapThreshold * 6,
            baseX + overlapThreshold * 9
        };

        for (int i = 0; i < cables.length; i++) {
            cables[i] =
                    new BossFightEntity(cableTexture, 1.0f, new float[] {cablePositions[i], 200f});
        }

        Scissors scissors =
                new Scissors(new Texture("character.png"), 1.0f, new float[] {100f, 100f});

        // Initially not in final stage
        assertFalse(bossFightLogic.isFinalStage());

        // Cut all cables one by one by positioning scissors at each cable's exact position
        // This ensures overlap
        for (int i = 0; i < cables.length; i++) {
            scissors.setX(cablePositions[i]);
            bossFightLogic.checkOverlap(cables, scissors);
        }

        // After cutting all 4 cables, final stage should be triggered
        assertTrue(
                bossFightLogic.isFinalStage(),
                "Final stage should be true after all cables are cut");
    }
}
