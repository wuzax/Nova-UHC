package net.novaproject.lguhc.win;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WinLguhcTest {

    @Test
    void loupsWinWhenVillageIsGoneEvenIfSolitairesRemain() {
        WinLguhc.Outcome outcome = WinLguhc.evaluate(new WinLguhc.Snapshot(0, 3, 1, false));
        assertTrue(outcome.win());
        assertEquals(WinLguhc.Kind.LOUPS, outcome.kind());
    }

    @Test
    void villageWinsWhenLoupsAreGone() {
        WinLguhc.Outcome outcome = WinLguhc.evaluate(new WinLguhc.Snapshot(4, 0, 1, false));
        assertTrue(outcome.win());
        assertEquals(WinLguhc.Kind.VILLAGE, outcome.kind());
    }

    @Test
    void noWinWhileBothCampsLive() {
        WinLguhc.Outcome outcome = WinLguhc.evaluate(new WinLguhc.Snapshot(3, 2, 0, false));
        assertFalse(outcome.win());
        assertEquals(WinLguhc.Kind.NONE, outcome.kind());
    }

    @Test
    void coupleWinsWhenBondCoversEveryoneAlive() {
        WinLguhc.Outcome outcome = WinLguhc.evaluate(new WinLguhc.Snapshot(1, 1, 0, true));
        assertTrue(outcome.win());
        assertEquals(WinLguhc.Kind.COUPLE, outcome.kind());
    }

    @Test
    void solitairesWinWhenMilitaryCampsAreGone() {
        WinLguhc.Outcome outcome = WinLguhc.evaluate(new WinLguhc.Snapshot(0, 0, 2, false));
        assertTrue(outcome.win());
        assertEquals(WinLguhc.Kind.SOLITAIRE, outcome.kind());
    }

    @Test
    void emptyBoardEndsWithoutACamp() {
        WinLguhc.Outcome outcome = WinLguhc.evaluate(new WinLguhc.Snapshot(0, 0, 0, false));
        assertTrue(outcome.win());
        assertEquals(WinLguhc.Kind.NONE, outcome.kind());
    }
}
