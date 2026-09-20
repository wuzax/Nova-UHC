package net.novaproject.uhc3945.knowledge;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PlayerKnowledge {

    private final UUID playerId;
    private final Set<UUID> knownAllies = new LinkedHashSet<>();
    private final Set<UUID> knownContacts = new LinkedHashSet<>();
    private final Set<String> knownCellIds = new LinkedHashSet<>();
    private final Map<String, AuthState> cellAuth = new LinkedHashMap<>();
    private final Set<String> interceptedSignals = new LinkedHashSet<>();
    private boolean kpiApplied;

    public PlayerKnowledge(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Set<UUID> getKnownAllies() {
        return Collections.unmodifiableSet(knownAllies);
    }

    public Set<UUID> getKnownContacts() {
        return Collections.unmodifiableSet(knownContacts);
    }

    public Set<String> getKnownCellIds() {
        return Collections.unmodifiableSet(knownCellIds);
    }

    public Set<String> getInterceptedSignals() {
        return Collections.unmodifiableSet(interceptedSignals);
    }

    public boolean addAlly(UUID uuid) {
        return uuid != null && !uuid.equals(playerId) && knownAllies.add(uuid);
    }

    public boolean addContact(UUID uuid) {
        if (uuid == null || uuid.equals(playerId) || knownAllies.contains(uuid)) {
            return false;
        }
        return knownContacts.add(uuid);
    }

    public void addCell(String cellId) {
        if (cellId != null) {
            knownCellIds.add(cellId);
        }
    }

    public void addInterceptedSignal(String signal) {
        if (signal != null && !signal.isEmpty()) {
            interceptedSignals.add(signal);
        }
    }

    public boolean hasIntercepted(String signal) {
        return signal != null && interceptedSignals.contains(signal);
    }

    public AuthState authOf(String cellId) {
        return cellAuth.getOrDefault(cellId, AuthState.NONE);
    }

    public void setAuth(String cellId, AuthState state) {
        if (cellId != null && state != null) {
            cellAuth.put(cellId, state);
        }
    }

    public Map<String, AuthState> authStates() {
        return Collections.unmodifiableMap(cellAuth);
    }

    public boolean isKpiApplied() {
        return kpiApplied;
    }

    public void markKpiApplied() {
        this.kpiApplied = true;
    }
}
