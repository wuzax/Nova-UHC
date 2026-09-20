package net.novaproject.uhc3945.win;

import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PersonalObjectiveTracker {

    private final Map<UUID, Set<String>> completed = new LinkedHashMap<>();
    private final Map<UUID, Set<String>> failed = new LinkedHashMap<>();

    public void complete(UUID playerId, String objectiveId) {
        if (playerId == null) return;
        String id = normalize(objectiveId);
        if (id.isEmpty() || isFailed(playerId, id)) return;
        if (completed.computeIfAbsent(playerId, k -> new LinkedHashSet<>()).add(id)) {
            Bukkit.getLogger().info("[UHC 39-45] Objectif personnel '" + id + "' accompli (" + playerId + ").");
        }
    }

    public void fail(UUID playerId, String objectiveId) {
        if (playerId == null) return;
        String id = normalize(objectiveId);
        if (id.isEmpty()) return;
        completed.getOrDefault(playerId, Collections.emptySet()).remove(id);
        if (failed.computeIfAbsent(playerId, k -> new LinkedHashSet<>()).add(id)) {
            Bukkit.getLogger().info("[UHC 39-45] Objectif personnel '" + id + "' échoué (" + playerId + ").");
        }
    }

    public boolean isCompleted(UUID playerId, String objectiveId) {
        if (playerId == null) return false;
        return completed.getOrDefault(playerId, Collections.emptySet()).contains(normalize(objectiveId));
    }

    public boolean isFailed(UUID playerId, String objectiveId) {
        if (playerId == null) return false;
        return failed.getOrDefault(playerId, Collections.emptySet()).contains(normalize(objectiveId));
    }

    public boolean hasPersonalWin(UUID playerId, PersonalObjective objective, boolean alive) {
        if (playerId == null || objective == null) return false;
        if (isFailed(playerId, objective.id())) return false;
        if (isCompleted(playerId, objective.id())) return true;
        return alive && objective.surviveCounts();
    }

    public Set<String> completedIds(UUID playerId) {
        if (playerId == null) return Set.of();
        return Collections.unmodifiableSet(completed.getOrDefault(playerId, Set.of()));
    }

    public void reset() {
        completed.clear();
        failed.clear();
    }

    private static String normalize(String objectiveId) {
        return objectiveId == null ? "" : objectiveId.trim().toLowerCase();
    }
}
