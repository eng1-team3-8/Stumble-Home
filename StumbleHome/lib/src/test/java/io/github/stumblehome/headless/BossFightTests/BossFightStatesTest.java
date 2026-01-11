package io.github.stumblehome.headless.BossFightTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.stumblehome.BossFight.BossFightStates;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.Test;

public class BossFightStatesTest extends AbstractHeadlessGdxTest {

    @Test
    public void testAllStatesExist() {
        // Verify all expected states exist in the enum
        assertNotNull(BossFightStates.INFO);
        assertNotNull(BossFightStates.OPTIONS);
        assertNotNull(BossFightStates.ATTACK);
        assertNotNull(BossFightStates.FINALATTACK);
        assertNotNull(BossFightStates.WIN);
        assertNotNull(BossFightStates.LOST);
    }

    @Test
    public void testStateValues() {
        // Verify enum values can be compared
        assertEquals(BossFightStates.INFO, BossFightStates.INFO);
        assertEquals(BossFightStates.OPTIONS, BossFightStates.OPTIONS);
        assertEquals(BossFightStates.ATTACK, BossFightStates.ATTACK);
        assertEquals(BossFightStates.FINALATTACK, BossFightStates.FINALATTACK);
        assertEquals(BossFightStates.WIN, BossFightStates.WIN);
        assertEquals(BossFightStates.LOST, BossFightStates.LOST);
    }

    @Test
    public void testStateInequality() {
        // Verify different states are not equal
        assertNotEquals(BossFightStates.INFO, BossFightStates.OPTIONS);
        assertNotEquals(BossFightStates.OPTIONS, BossFightStates.ATTACK);
        assertNotEquals(BossFightStates.ATTACK, BossFightStates.FINALATTACK);
        assertNotEquals(BossFightStates.FINALATTACK, BossFightStates.WIN);
        assertNotEquals(BossFightStates.WIN, BossFightStates.LOST);
    }

    @Test
    public void testEnumToString() {
        // Verify enum has proper string representation
        assertNotNull(BossFightStates.INFO.toString());
        assertNotNull(BossFightStates.OPTIONS.toString());
        assertNotNull(BossFightStates.ATTACK.toString());
        assertNotNull(BossFightStates.FINALATTACK.toString());
        assertNotNull(BossFightStates.WIN.toString());
        assertNotNull(BossFightStates.LOST.toString());
    }

    @Test
    public void testEnumValueOf() {
        // Verify enum can be retrieved by name
        assertEquals(BossFightStates.INFO, BossFightStates.valueOf("INFO"));
        assertEquals(BossFightStates.OPTIONS, BossFightStates.valueOf("OPTIONS"));
        assertEquals(BossFightStates.ATTACK, BossFightStates.valueOf("ATTACK"));
        assertEquals(BossFightStates.FINALATTACK, BossFightStates.valueOf("FINALATTACK"));
        assertEquals(BossFightStates.WIN, BossFightStates.valueOf("WIN"));
        assertEquals(BossFightStates.LOST, BossFightStates.valueOf("LOST"));
    }

    @Test
    public void testEnumValues() {
        // Verify all values are present
        BossFightStates[] values = BossFightStates.values();
        assertEquals(6, values.length, "Should have exactly 6 states");
        assertTrue(
                java.util.Arrays.asList(values).contains(BossFightStates.INFO),
                "Should have INFO state");
        assertTrue(
                java.util.Arrays.asList(values).contains(BossFightStates.OPTIONS),
                "Should have OPTIONS state");
        assertTrue(
                java.util.Arrays.asList(values).contains(BossFightStates.ATTACK),
                "Should have ATTACK state");
        assertTrue(
                java.util.Arrays.asList(values).contains(BossFightStates.FINALATTACK),
                "Should have FINALATTACK state");
        assertTrue(
                java.util.Arrays.asList(values).contains(BossFightStates.WIN),
                "Should have WIN state");
        assertTrue(
                java.util.Arrays.asList(values).contains(BossFightStates.LOST),
                "Should have LOST state");
    }
}
