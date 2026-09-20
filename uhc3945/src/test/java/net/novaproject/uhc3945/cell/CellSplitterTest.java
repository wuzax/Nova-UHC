package net.novaproject.uhc3945.cell;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CellSplitterTest {

    @Test
    void packsTypicalSizes() {
        assertSizes(1, false, List.of(1));
        assertSizes(2, false, List.of(2));
        assertSizes(3, false, List.of(3));
        assertSizes(4, false, List.of(2, 2));
        assertSizes(5, false, List.of(3, 2));
        assertSizes(6, false, List.of(3, 3));
        assertSizes(7, false, List.of(3, 2, 2));
        assertSizes(8, false, List.of(3, 3, 2));
        assertSizes(9, false, List.of(3, 3, 3));
        assertSizes(9, true, List.of(3, 3, 2, 1));
        assertSizes(12, true, List.of(3, 3, 3, 2, 1));
    }

    @Test
    void chefIsNotIsolatedWhenANetworkExists() {
        List<UUID> members = ids(9);
        UUID chef = members.get(0);
        List<ResistanceCell> cells = CellSplitter.split(members, chef, new Random(2), true);
        ResistanceCell chefCell = cells.stream().filter(c -> c.contains(chef)).findFirst().orElseThrow();
        assertFalse(chefCell.isIsolate());
        assertEquals(chef, chefCell.getChefId());
        assertTrue(chefCell.getMembers().size() >= 2);
    }

    private static void assertSizes(int n, boolean isolate, List<Integer> expected) {
        List<ResistanceCell> cells = CellSplitter.split(ids(n), null, new Random(1), isolate);
        List<Integer> sizes = cells.stream()
                .map(c -> c.getMembers().size())
                .sorted((a, b) -> Integer.compare(b, a))
                .collect(Collectors.toList());
        List<Integer> expectedSorted = new ArrayList<>(expected);
        expectedSorted.sort((a, b) -> Integer.compare(b, a));
        assertEquals(expectedSorted, sizes, "n=" + n + " isolate=" + isolate);
        int isolates = (int) cells.stream().filter(ResistanceCell::isIsolate).count();
        long ones = sizes.stream().filter(s -> s == 1).count();
        assertEquals(ones, isolates);
        cells.stream().filter(c -> !c.isIsolate()).forEach(c ->
                assertTrue(c.getMembers().size() >= 2 && c.getMembers().size() <= 3));
    }

    private static List<UUID> ids(int n) {
        List<UUID> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(new UUID(0, i + 1));
        }
        return list;
    }
}
