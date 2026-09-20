package net.novaproject.uhc3945.auth;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthCodesTest {

    @Test
    void normalizeStripsAccentsAndSeparators() {
        assertEquals("BRUME", AuthCodes.normalize(" brûmé "));
        assertEquals("CHENE", AuthCodes.normalize("Chêne"));
        assertEquals("BRUME LANTERNE", AuthCodes.normalize("brume-lanterne"));
        assertEquals("BRUME LANTERNE", AuthCodes.normalize("BRUME_LANTERNE"));
        assertArrayEquals(new String[]{"BRUME", "LANTERNE"}, AuthCodes.parts("BRUME LANTERNE"));
    }

    @Test
    void uniquePairsDoNotReuseSignals() {
        List<String[]> pairs = AuthCodes.uniquePairs(12, new Random(3));
        Set<String> signals = new HashSet<>();
        for (String[] pair : pairs) {
            assertTrue(signals.add(pair[0]));
            assertEquals(2, pair.length);
        }
        assertEquals(12, signals.size());
    }
}
