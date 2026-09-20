package net.novaproject.uhc3945.cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class CellSplitter {

    static final List<String> NETWORK_NAMES = List.of(
            "Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel",
            "India", "Juliett", "Kilo", "Lima");

    private CellSplitter() {
    }

    public static List<ResistanceCell> split(List<UUID> members, UUID preferredChef,
                                             Random random, boolean isolateWhenDivisibleBy3) {
        if (members == null || members.isEmpty()) {
            return List.of();
        }
        List<UUID> pool = new ArrayList<>(members);
        Collections.shuffle(pool, random == null ? new Random() : random);

        List<List<UUID>> groups = new ArrayList<>();
        int total = pool.size();
        if (isolateWhenDivisibleBy3 && total >= 7 && total % 3 == 0) {
            int isolateAt = lastIndexNotEqual(pool, preferredChef);
            groups.add(new ArrayList<>(List.of(pool.remove(isolateAt))));
        }

        while (!pool.isEmpty()) {
            int left = pool.size();
            int take = left == 4 || left == 2 ? 2 : (left == 1 ? 1 : 3);
            List<UUID> group = new ArrayList<>(take);
            for (int i = 0; i < take; i++) {
                group.add(pool.remove(0));
            }
            groups.add(group);
        }

        keepChefInANetwork(groups, preferredChef);

        List<ResistanceCell> cells = new ArrayList<>(groups.size());
        int nameIndex = 0;
        for (List<UUID> group : groups) {
            boolean isolate = group.size() == 1;
            String name = isolate ? "Isolé" : NETWORK_NAMES.get(nameIndex++ % NETWORK_NAMES.size());
            String id = isolate ? "isolate-" + group.get(0) : name.toLowerCase();
            ResistanceCell cell = new ResistanceCell(id, name, group, isolate);
            if (preferredChef != null && cell.contains(preferredChef) && !isolate) {
                cell.setChefId(preferredChef);
            }
            cells.add(cell);
        }
        return cells;
    }

    private static void keepChefInANetwork(List<List<UUID>> groups, UUID chef) {
        if (chef == null) {
            return;
        }
        List<UUID> chefGroup = null;
        List<UUID> host = null;
        for (List<UUID> group : groups) {
            if (group.contains(chef)) {
                chefGroup = group;
            } else if (group.size() >= 2 && (host == null || group.size() > host.size())) {
                host = group;
            }
        }
        if (chefGroup != null && chefGroup.size() == 1 && host != null) {
            UUID swapped = host.get(0);
            host.set(0, chef);
            chefGroup.set(0, swapped);
        }
    }

    private static int lastIndexNotEqual(List<UUID> pool, UUID excluded) {
        for (int i = pool.size() - 1; i >= 0; i--) {
            if (excluded == null || !excluded.equals(pool.get(i))) {
                return i;
            }
        }
        return pool.size() - 1;
    }
}
