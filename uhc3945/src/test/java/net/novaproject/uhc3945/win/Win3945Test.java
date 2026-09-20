package net.novaproject.uhc3945.win;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Win3945Test {

    private static final Win3945.Flags DEFAULTS = Win3945.Flags.defaults();

    @Test
    void axeWinsWhenResistanceIsGoneEvenIfCiviliansRemain() {
        Win3945.Outcome outcome = Win3945.evaluate(alive(3, 0, 2), DEFAULTS);
        assertTrue(outcome.win());
        assertEquals(Win3945.Kind.AXE, outcome.kind());
        assertTrue(outcome.hasCivilianShare(DEFAULTS, 2));
    }

    @Test
    void resistanceWinsWhenAxeIsGone() {
        Win3945.Outcome outcome = Win3945.evaluate(alive(0, 4, 1), DEFAULTS);
        assertTrue(outcome.win());
        assertEquals(Win3945.Kind.RESISTANCE, outcome.kind());
    }

    @Test
    void militaryWinIsBlockedWhenCiviliansMustDie() {
        Win3945.Flags flags = new Win3945.Flags(true, false, false, true, true);
        Win3945.Outcome outcome = Win3945.evaluate(alive(2, 0, 1), flags);
        assertFalse(outcome.win());
        assertEquals(Win3945.Kind.NONE, outcome.kind());
    }

    @Test
    void civiliansWinWhenBothMilitaryCampsAreGone() {
        Win3945.Outcome outcome = Win3945.evaluate(alive(0, 0, 3), DEFAULTS);
        assertTrue(outcome.win());
        assertEquals(Win3945.Kind.CIVILIANS, outcome.kind());
    }

    @Test
    void noWinWhileBothMilitaryCampsAreAlive() {
        Win3945.Outcome outcome = Win3945.evaluate(alive(2, 2, 5), DEFAULTS);
        assertFalse(outcome.win());
        assertEquals(Win3945.Kind.NONE, outcome.kind());
    }

    @Test
    void commandantRequirementIsIgnoredWhenNoOppositionRemains() {
        Win3945.Flags flags = new Win3945.Flags(false, true, false, true, true);
        Win3945.Snapshot snapshot = new Win3945.Snapshot(2, 0, 0, 0, false, true, true);
        Win3945.Outcome outcome = Win3945.evaluate(snapshot, flags);
        assertTrue(outcome.win());
        assertEquals(Win3945.Kind.AXE, outcome.kind());
    }

    @Test
    void commandantRequirementCanStallIfForcedEndIsDisabled() {
        Win3945.Flags flags = new Win3945.Flags(false, true, false, false, true);
        Win3945.Snapshot snapshot = new Win3945.Snapshot(2, 0, 0, 0, false, true, true);
        Win3945.Outcome outcome = Win3945.evaluate(snapshot, flags);
        assertFalse(outcome.win());
    }

    @Test
    void resistanceScenarioObjectivesDoNotStallByDefault() {
        Win3945.Flags flags = new Win3945.Flags(false, false, true, true, true);
        Win3945.Snapshot snapshot = new Win3945.Snapshot(0, 3, 0, 0, false, false, true);
        Win3945.Outcome outcome = Win3945.evaluate(snapshot, flags);
        assertTrue(outcome.win());
        assertEquals(Win3945.Kind.RESISTANCE, outcome.kind());
    }

    @Test
    void resistanceScenarioObjectivesCanBlockIfForcedEndIsDisabled() {
        Win3945.Flags flags = new Win3945.Flags(false, false, true, false, true);
        Win3945.Snapshot snapshot = new Win3945.Snapshot(0, 3, 0, 0, false, false, true);
        assertFalse(Win3945.evaluate(snapshot, flags).win());
        Win3945.Snapshot ready = new Win3945.Snapshot(0, 3, 0, 0, false, true, true);
        assertTrue(Win3945.evaluate(ready, flags).win());
    }

    @Test
    void unassignedPlayersPreventACampWin() {
        Win3945.Snapshot snapshot = new Win3945.Snapshot(3, 0, 0, 1, true, true, true);
        assertFalse(Win3945.evaluate(snapshot, DEFAULTS).win());
    }

    @Test
    void emptyBoardIsADraw() {
        Win3945.Outcome outcome = Win3945.evaluate(alive(0, 0, 0), DEFAULTS);
        assertTrue(outcome.win());
        assertEquals(Win3945.Kind.NONE, outcome.kind());
    }

    private static Win3945.Snapshot alive(int axe, int resistance, int civilians) {
        return new Win3945.Snapshot(axe, resistance, civilians, 0, true, true, true);
    }
}
