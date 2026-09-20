package net.novaproject.uhc3945.cell;

import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.roles.ChefReseau;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public final class CellService {

    private final Map<String, ResistanceCell> cells = new LinkedHashMap<>();
    private final Map<UUID, ResistanceCell> byMember = new LinkedHashMap<>();

    public void clear() {
        cells.clear();
        byMember.clear();
    }

    public void assign(Map<UHCPlayer, Role> roles, boolean isolateWhenDivisibleBy3, Random random) {
        clear();
        List<UUID> resistance = new ArrayList<>();
        UUID chef = null;
        for (Map.Entry<UHCPlayer, Role> entry : roles.entrySet()) {
            UHCPlayer player = entry.getKey();
            Role role = entry.getValue();
            if (player == null || role == null || role.getCamp() == null) {
                continue;
            }
            if (!role.getCamp().is(Camps3945.RESISTANCE)) {
                continue;
            }
            resistance.add(player.getUuid());
            if (role instanceof ChefReseau) {
                chef = player.getUuid();
            }
        }
        for (ResistanceCell cell : CellSplitter.split(resistance, chef, random, isolateWhenDivisibleBy3)) {
            register(cell);
        }
        logAssignment();
    }

    private void register(ResistanceCell cell) {
        cells.put(cell.getId(), cell);
        for (UUID member : cell.getMembers()) {
            byMember.put(member, cell);
        }
    }

    public ResistanceCell cellOf(UUID playerId) {
        return playerId == null ? null : byMember.get(playerId);
    }

    public ResistanceCell byId(String id) {
        return id == null ? null : cells.get(id);
    }

    public ResistanceCell bySignal(String signal) {
        if (signal == null || signal.isEmpty()) {
            return null;
        }
        for (ResistanceCell cell : cells.values()) {
            if (signal.equals(cell.getSignal())) {
                return cell;
            }
        }
        return null;
    }

    public Collection<ResistanceCell> all() {
        return Collections.unmodifiableCollection(cells.values());
    }

    public boolean sameCell(UUID a, UUID b) {
        ResistanceCell cell = cellOf(a);
        return cell != null && cell.contains(b);
    }

    private void logAssignment() {
        if (cells.isEmpty()) {
            Bukkit.getLogger().info("[UHC 39-45] Aucune cellule Résistance (aucun rôle du camp attribué).");
            return;
        }
        for (ResistanceCell cell : cells.values()) {
            Bukkit.getLogger().info("[UHC 39-45] "
                    + (cell.isIsolate() ? "Agent isolé" : "Cellule " + cell.getName())
                    + " (" + cell.getMembers().size() + ") ids=" + cell.getMembers()
                    + (cell.getChefId() != null ? " chef=" + cell.getChefId() : ""));
        }
    }
}
