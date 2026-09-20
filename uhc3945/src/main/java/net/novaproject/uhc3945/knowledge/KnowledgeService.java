package net.novaproject.uhc3945.knowledge;

import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.RoleDescription;
import net.novaproject.novauhc.scenario.role.reveal.KPIBuilder;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import net.novaproject.uhc3945.cell.CellService;
import net.novaproject.uhc3945.cell.ResistanceCell;
import net.novaproject.uhc3945.roles.Commandant;
import net.novaproject.uhc3945.roles.Infiltre;
import net.novaproject.uhc3945.roles.Officier;
import net.novaproject.uhc3945.roles.Soldat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class KnowledgeService {

    private final Map<UUID, PlayerKnowledge> byPlayer = new LinkedHashMap<>();
    private boolean hasInfiltre;

    public void clear() {
        byPlayer.clear();
        hasInfiltre = false;
    }

    public PlayerKnowledge of(UUID playerId) {
        if (playerId == null) {
            return null;
        }
        return byPlayer.computeIfAbsent(playerId, PlayerKnowledge::new);
    }

    public void grantInitial(Map<UHCPlayer, Role> roles, CellService cells) {
        List<UUID> axeCore = new ArrayList<>();
        boolean hasInfiltre = false;
        for (Map.Entry<UHCPlayer, Role> entry : roles.entrySet()) {
            Role role = entry.getValue();
            if (isAxeCore(role)) {
                axeCore.add(entry.getKey().getUuid());
            }
            if (role instanceof Infiltre) {
                hasInfiltre = true;
            }
        }

        for (Map.Entry<UHCPlayer, Role> entry : roles.entrySet()) {
            UUID id = entry.getKey().getUuid();
            Role role = entry.getValue();
            PlayerKnowledge knowledge = of(id);
            if (role.getCamp() != null && role.getCamp().is(Camps3945.AXE) && isAxeCore(role)) {
                for (UUID ally : axeCore) {
                    knowledge.addAlly(ally);
                }
            }
            ResistanceCell cell = cells.cellOf(id);
            if (cell != null) {
                knowledge.addCell(cell.getId());
                for (UUID member : cell.getMembers()) {
                    knowledge.addAlly(member);
                }
            }
        }

        this.hasInfiltre = hasInfiltre;
    }

    public void applyToRole(Role role) {
        if (role == null || role.getOwner() == null) {
            return;
        }
        UUID id = role.getOwner().getUuid();
        PlayerKnowledge knowledge = of(id);
        if (knowledge.isKpiApplied()) {
            return;
        }
        knowledge.markKpiApplied();

        if (!knowledge.getKnownAllies().isEmpty()) {
            role.addKnowPlayer(KPIBuilder.all()
                    .showRole()
                    .name(allyHeader(role))
                    .filter(up -> knowledge.getKnownAllies().contains(up.getUuid()))
                    .build(role));
        }
        if (!knowledge.getKnownContacts().isEmpty()) {
            role.addKnowPlayer(KPIBuilder.all()
                    .name(LangManager.get().get(Lang3945.KNOWLEDGE_CONTACTS_HEADER))
                    .filter(up -> knowledge.getKnownContacts().contains(up.getUuid()))
                    .build(role));
        }

        Player player = role.getOwner().getPlayer();
        if (player != null && player.isOnline()) {
            sendFacts(player, role, knowledge);
        }
    }

    public void grantAuthenticatedNetwork(UUID viewerId, ResistanceCell other, Role viewerRole) {
        if (viewerId == null || other == null) {
            return;
        }
        PlayerKnowledge knowledge = of(viewerId);
        knowledge.addCell(other.getId());
        knowledge.setAuth(other.getId(), AuthState.AUTHENTICATED);
        boolean added = false;
        for (UUID member : other.getMembers()) {
            added |= knowledge.addContact(member);
        }
        if (added && viewerRole != null && !knowledge.getKnownContacts().isEmpty()) {
            viewerRole.addKnowPlayer(KPIBuilder.all()
                    .name(LangManager.get().get(Lang3945.KNOWLEDGE_AUTH_CELL_HEADER,
                            Map.of("%cell%", other.getName())))
                    .filter(up -> other.contains(up.getUuid()) && !up.getUuid().equals(viewerId))
                    .build(viewerRole));
        }
    }

    public void sendContacts(Player player, Role role, CellService cells) {
        LangManager lang = LangManager.get();
        if (player == null || role == null || role.getOwner() == null) {
            return;
        }
        PlayerKnowledge knowledge = of(role.getOwner().getUuid());
        lang.send(Lang3945.CONTACTS_HEADER, player);

        ResistanceCell own = cells.cellOf(role.getOwner().getUuid());
        if (own != null) {
            lang.send(own.isIsolate() ? Lang3945.CONTACTS_OWN_ISOLATE : Lang3945.CONTACTS_OWN_CELL, player,
                    Map.of("%cell%", own.getName(), "%size%", own.getMembers().size()));
        }

        if (knowledge.getKnownAllies().isEmpty() && knowledge.getKnownContacts().isEmpty()) {
            lang.send(Lang3945.CONTACTS_NONE, player);
        } else {
            role.sendKnowPlayers();
        }

        knowledge.authStates().forEach((cellId, state) -> {
            ResistanceCell cell = cells.byId(cellId);
            if (cell == null || (own != null && cell.getId().equals(own.getId()))) {
                return;
            }
            lang.send(Lang3945.CONTACTS_AUTH_LINE, player, Map.of(
                    "%cell%", cell.getName(),
                    "%state%", authLabel(state)));
        });
        if (!knowledge.getInterceptedSignals().isEmpty()) {
            lang.send(Lang3945.CONTACTS_INTERCEPTED, player,
                    Map.of("%signals%", String.join(", ", knowledge.getInterceptedSignals())));
        }
    }

    public void sendCamp(Player player, Role role, CellService cells) {
        LangManager lang = LangManager.get();
        if (player == null || role == null || role.getCamp() == null) {
            lang.send(Lang3945.CAMP_UNKNOWN, player);
            return;
        }
        if (role.getCamp().is(Camps3945.AXE)) {
            if (role instanceof Infiltre) {
                lang.send(Lang3945.CAMP_AXE_ISOLATED, player);
            } else {
                lang.send(Lang3945.CAMP_AXE_CORE, player);
            }
            return;
        }
        if (role.getCamp().is(Camps3945.RESISTANCE)) {
            ResistanceCell cell = role.getOwner() == null ? null : cells.cellOf(role.getOwner().getUuid());
            if (cell == null) {
                lang.send(Lang3945.CAMP_RESISTANCE, player);
            } else if (cell.isIsolate()) {
                lang.send(Lang3945.CAMP_RESISTANCE_ISOLATE, player);
            } else {
                lang.send(Lang3945.CAMP_RESISTANCE_CELL, player, Map.of("%cell%", cell.getName()));
            }
            return;
        }
        lang.send(Lang3945.CAMP_CIVILIAN, player);
    }

    public void appendRoleCard(RoleDescription features, Player player, Role role, CellService cells) {
        if (role.getOwner() == null) {
            return;
        }
        ResistanceCell cell = cells.cellOf(role.getOwner().getUuid());
        if (cell != null && !cell.isIsolate()) {
            features.line(Lang3945.ROLE_CARD_CELL, Map.of("%cell%", cell.getName()));
        } else if (cell != null) {
            features.line(Lang3945.ROLE_CARD_ISOLATE);
        }
    }

    private static boolean isAxeCore(Role role) {
        return role instanceof Commandant || role instanceof Soldat || role instanceof Officier;
    }

    private void sendFacts(Player player, Role role, PlayerKnowledge knowledge) {
        LangManager lang = LangManager.get();
        if (role.getCamp() != null && role.getCamp().is(Camps3945.AXE)) {
            if (role instanceof Infiltre) {
                lang.send(Lang3945.FACT_AXE_ISOLATED, player);
            } else {
                lang.send(Lang3945.FACT_AXE_CORE, player);
                if (hasInfiltre && role instanceof Commandant) {
                    lang.send(Lang3945.FACT_AXE_INFILTRE_EXISTS, player);
                }
            }
        }
        if (role.getCamp() != null && role.getCamp().is(Camps3945.RESISTANCE)) {
            if (knowledge.getKnownAllies().isEmpty()) {
                lang.send(Lang3945.FACT_RESISTANCE_ISOLATE, player);
            } else {
                lang.send(Lang3945.FACT_RESISTANCE_FRAGMENTED, player);
            }
        }
    }

    private String allyHeader(Role role) {
        if (role.getCamp() != null && role.getCamp().is(Camps3945.AXE)) {
            return LangManager.get().get(Lang3945.KNOWLEDGE_AXE_CORE_HEADER);
        }
        if (role.getOwner() != null && Scenario3945.get() != null) {
            ResistanceCell cell = Scenario3945.get().cells().cellOf(role.getOwner().getUuid());
            if (cell != null && !cell.isIsolate()) {
                return LangManager.get().get(Lang3945.KNOWLEDGE_AUTH_CELL_HEADER,
                        Map.of("%cell%", cell.getName()));
            }
        }
        return LangManager.get().get(Lang3945.KNOWLEDGE_CELL_HEADER);
    }

    private static String authLabel(AuthState state) {
        return switch (state) {
            case PARTIAL -> LangManager.get().get(Lang3945.AUTH_STATE_PARTIAL);
            case PENDING -> LangManager.get().get(Lang3945.AUTH_STATE_PENDING);
            case AUTHENTICATED -> LangManager.get().get(Lang3945.AUTH_STATE_AUTHENTICATED);
            case SUSPECTED -> LangManager.get().get(Lang3945.AUTH_STATE_SUSPECTED);
            default -> LangManager.get().get(Lang3945.AUTH_STATE_NONE);
        };
    }

    public static String displayName(UUID uuid) {
        if (uuid == null) {
            return "?";
        }
        UHCPlayer up = UHCPlayerManager.get().getPlayer(uuid);
        if (up == null) {
            return "?";
        }
        Player player = up.getPlayer();
        return player != null ? player.getName() : "?";
    }
}
