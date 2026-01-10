package io.github.stumblehome.headless.BossFightTests;

import static org.junit.jupiter.api.Assertions.*;

import io.github.stumblehome.BossFight.BossFightStates;
import io.github.stumblehome.BossFight.BossFightStatesManager;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BossFightStatesManagerTest extends AbstractHeadlessGdxTest {
    private BossFightStatesManager stateManager;

    @BeforeEach
    public void setUp() {
        stateManager = new BossFightStatesManager();
    }

    @Test
    public void testInitialState() {
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());
    }

    @Test
    public void testMoveFromOptionsToAttack() {
        stateManager.moveStates(1);
        assertEquals(BossFightStates.ATTACK, stateManager.returnState());
    }

    @Test
    public void testMoveFromOptionsToInfo() {
        stateManager.moveStates(-1);
        assertEquals(BossFightStates.INFO, stateManager.returnState());
    }

    @Test
    public void testMoveFromOptionsToFinalAttack() {
        stateManager.moveStates(0);
        assertEquals(BossFightStates.FINALATTACK, stateManager.returnState());
    }

    @Test
    public void testMoveFromInfoToOptions() {
        stateManager.moveStates(-1); // Move to INFO first
        assertEquals(BossFightStates.INFO, stateManager.returnState());
        // From INFO, any valid move goes to OPTIONS (no switch case for move value, always goes to
        // OPTIONS)
        stateManager.moveStates(1); // Should move to OPTIONS
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());

        // Test with move 0 as well
        stateManager.moveStates(-1); // Back to INFO
        assertEquals(BossFightStates.INFO, stateManager.returnState());
        stateManager.moveStates(0); // Should also move to OPTIONS
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());
    }

    @Test
    public void testMoveFromAttackToOptions() {
        stateManager.moveStates(1); // Move to ATTACK
        assertEquals(BossFightStates.ATTACK, stateManager.returnState());
        stateManager.moveStates(1); // Move back to OPTIONS
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());
    }

    @Test
    public void testMoveFromAttackToLost() {
        stateManager.moveStates(1); // Move to ATTACK
        assertEquals(BossFightStates.ATTACK, stateManager.returnState());
        stateManager.moveStates(0); // Move to LOST
        assertEquals(BossFightStates.LOST, stateManager.returnState());
    }

    @Test
    public void testMoveFromFinalAttackToWin() {
        stateManager.moveStates(0); // Move to FINALATTACK
        assertEquals(BossFightStates.FINALATTACK, stateManager.returnState());
        stateManager.moveStates(1); // Move to WIN
        assertEquals(BossFightStates.WIN, stateManager.returnState());
    }

    @Test
    public void testMoveFromFinalAttackToOptions() {
        stateManager.moveStates(0); // Move to FINALATTACK
        assertEquals(BossFightStates.FINALATTACK, stateManager.returnState());
        stateManager.moveStates(-1); // Move back to OPTIONS
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());
    }

    @Test
    public void testMoveFromFinalAttackToLost() {
        stateManager.moveStates(0); // Move to FINALATTACK
        assertEquals(BossFightStates.FINALATTACK, stateManager.returnState());
        stateManager.moveStates(0); // Move to LOST
        assertEquals(BossFightStates.LOST, stateManager.returnState());
    }

    @Test
    public void testInvalidMoveGreaterThanOne() {
        BossFightStates initialState = stateManager.returnState();
        stateManager.moveStates(2);
        assertEquals(initialState, stateManager.returnState());
    }

    @Test
    public void testInvalidMoveLessThanMinusOne() {
        BossFightStates initialState = stateManager.returnState();
        stateManager.moveStates(-2);
        assertEquals(initialState, stateManager.returnState());
    }

    @Test
    public void testMultipleStateTransitions() {
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());
        stateManager.moveStates(1); // OPTIONS -> ATTACK
        assertEquals(BossFightStates.ATTACK, stateManager.returnState());
        stateManager.moveStates(1); // ATTACK -> OPTIONS
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());
        stateManager.moveStates(0); // OPTIONS -> FINALATTACK
        assertEquals(BossFightStates.FINALATTACK, stateManager.returnState());
        stateManager.moveStates(1); // FINALATTACK -> WIN
        assertEquals(BossFightStates.WIN, stateManager.returnState());
    }

    @Test
    public void testStateTransitionChain() {
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());
        stateManager.moveStates(-1); // OPTIONS -> INFO
        assertEquals(BossFightStates.INFO, stateManager.returnState());
        stateManager.moveStates(0); // INFO -> OPTIONS
        assertEquals(BossFightStates.OPTIONS, stateManager.returnState());
    }

    @Test
    public void testWinAndLostStatesAreTerminal() {
        // Move to WIN state
        stateManager.moveStates(0); // OPTIONS -> FINALATTACK
        stateManager.moveStates(1); // FINALATTACK -> WIN
        BossFightStates winState = stateManager.returnState();
        assertEquals(BossFightStates.WIN, winState);

        // Create new manager and test LOST state
        stateManager = new BossFightStatesManager();
        stateManager.moveStates(1); // OPTIONS -> ATTACK
        stateManager.moveStates(0); // ATTACK -> LOST
        assertEquals(BossFightStates.LOST, stateManager.returnState());
    }
}
