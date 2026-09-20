package net.novaproject.uhc3945.cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class ResistanceCell {

    private final String id;
    private final String name;
    private final List<UUID> members;
    private final boolean isolate;
    private UUID chefId;
    private String signal;
    private String secret;

    public ResistanceCell(String id, String name, List<UUID> members, boolean isolate) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>(members);
        this.isolate = isolate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<UUID> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public boolean isIsolate() {
        return isolate;
    }

    public UUID getChefId() {
        return chefId;
    }

    public void setChefId(UUID chefId) {
        this.chefId = chefId;
    }

    public String getSignal() {
        return signal;
    }

    public String getSecret() {
        return secret;
    }

    public void setCredentials(String signal, String secret) {
        this.signal = signal;
        this.secret = secret;
    }

    public boolean contains(UUID uuid) {
        return uuid != null && members.contains(uuid);
    }
}
