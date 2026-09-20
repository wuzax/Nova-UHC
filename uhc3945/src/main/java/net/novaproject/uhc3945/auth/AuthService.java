package net.novaproject.uhc3945.auth;

import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import net.novaproject.uhc3945.cell.CellService;
import net.novaproject.uhc3945.cell.ResistanceCell;
import net.novaproject.uhc3945.knowledge.AuthState;
import net.novaproject.uhc3945.knowledge.KnowledgeService;
import net.novaproject.uhc3945.knowledge.PlayerKnowledge;
import net.novaproject.uhc3945.roles.ChefReseau;
import net.novaproject.uhc3945.roles.Infiltre;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public final class AuthService {

    private final Map<UUID, Long> listeningUntil = new LinkedHashMap<>();

    public void clear() {
        listeningUntil.clear();
    }

    public void issueCredentials(CellService cells, Random random) {
        var pairs = AuthCodes.uniquePairs(cells.all().size(), random);
        int i = 0;
        for (ResistanceCell cell : cells.all()) {
            String[] pair = pairs.get(i++);
            cell.setCredentials(pair[0], pair[1]);
            Bukkit.getLogger().info("[UHC 39-45] Codes cellule " + cell.getName()
                    + " signal=" + pair[0] + " secret=" + pair[1]);
        }
    }

    public void startListening(UUID infiltreId, int seconds) {
        listeningUntil.put(infiltreId, System.currentTimeMillis() + Math.max(1, seconds) * 1000L);
    }

    public boolean isListening(UUID infiltreId) {
        Long until = listeningUntil.get(infiltreId);
        return until != null && until >= System.currentTimeMillis();
    }

    public boolean canSeeSecret(UHCPlayer player, ResistanceCell cell) {
        if (player == null || cell == null) {
            return false;
        }
        if (!cell.contains(player.getUuid())) {
            return false;
        }
        if (cell.isIsolate()) {
            return true;
        }
        UUID chef = livingChef(cell);
        if (chef == null) {
            return true;
        }
        return chef.equals(player.getUuid());
    }

    public boolean submitCode(Player player, String rawCode) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return false;
        }
        UHCPlayer up = UHCPlayerManager.get().getPlayer(player);
        if (up == null) {
            return false;
        }
        ResistanceCell own = scenario.cells().cellOf(up.getUuid());
        if (own == null) {
            LangManager.get().send(Lang3945.AUTH_NOT_RESISTANCE, player);
            return false;
        }
        String normalized = AuthCodes.normalize(rawCode);
        String[] parts = AuthCodes.parts(normalized);
        if (parts.length == 0) {
            LangManager.get().send(Lang3945.AUTH_USAGE, player);
            return false;
        }
        String signal = parts[0];
        String secret = parts.length >= 2 ? parts[1] : "";
        ResistanceCell target = scenario.cells().bySignal(signal);
        if (target == null) {
            LangManager.get().send(Lang3945.AUTH_UNKNOWN, player);
            leakIntercept(scenario, signal, false);
            return false;
        }
        if (target.getId().equals(own.getId())) {
            LangManager.get().send(Lang3945.AUTH_OWN_CELL, player);
            return false;
        }
        PlayerKnowledge knowledge = scenario.knowledge().of(up.getUuid());
        if (knowledge.authOf(target.getId()) == AuthState.AUTHENTICATED) {
            LangManager.get().send(Lang3945.AUTH_ALREADY, player, Map.of("%cell%", target.getName()));
            return false;
        }
        boolean full = !secret.isEmpty() && secret.equals(target.getSecret());
        if (!secret.isEmpty() && !full) {
            LangManager.get().send(Lang3945.AUTH_SECRET_MISMATCH, player);
            leakIntercept(scenario, signal, true);
            return true;
        }
        AuthState next = full ? AuthState.PENDING : AuthState.PARTIAL;
        knowledge.setAuth(target.getId(), next);
        leakIntercept(scenario, signal, true);
        if (full) {
            LangManager.get().send(Lang3945.AUTH_PENDING, player, Map.of("%cell%", target.getName()));
            notifyCell(target, Lang3945.AUTH_PENDING_NOTIFY, player.getName());
            Bukkit.getLogger().info("[UHC 39-45] Auth PENDING " + player.getName() + " → " + target.getName());
        } else {
            LangManager.get().send(Lang3945.AUTH_PARTIAL, player);
            Bukkit.getLogger().info("[UHC 39-45] Auth PARTIAL " + player.getName() + " → " + target.getName());
        }
        return true;
    }

    public boolean confirm(Player chefPlayer, String targetName) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return false;
        }
        UHCPlayer chefUp = UHCPlayerManager.get().getPlayer(chefPlayer);
        if (chefUp == null) {
            return false;
        }
        ResistanceCell own = scenario.cells().cellOf(chefUp.getUuid());
        if (own == null) {
            LangManager.get().send(Lang3945.AUTH_NOT_RESISTANCE, chefPlayer);
            return false;
        }
        if (!canConfirm(chefUp, own, scenario)) {
            LangManager.get().send(Lang3945.AUTH_CONFIRM_DENIED, chefPlayer);
            return false;
        }
        Player targetBukkit = targetName == null ? null : Bukkit.getPlayerExact(targetName);
        UHCPlayer target = targetBukkit == null ? null : UHCPlayerManager.get().getPlayer(targetBukkit);
        if (target == null || !target.isPlaying()) {
            LangManager.get().send(Lang3945.AUTH_PLAYER_NOT_FOUND, chefPlayer);
            return false;
        }
        if (own.contains(target.getUuid())) {
            LangManager.get().send(Lang3945.AUTH_CONFIRM_SAME_CELL, chefPlayer);
            return false;
        }
        PlayerKnowledge theirs = scenario.knowledge().of(target.getUuid());
        AuthState state = theirs.authOf(own.getId());
        if (state != AuthState.PARTIAL && state != AuthState.PENDING) {
            LangManager.get().send(Lang3945.AUTH_CONFIRM_NO_PENDING, chefPlayer);
            return false;
        }
        ResistanceCell other = scenario.cells().cellOf(target.getUuid());
        completeLink(scenario, own, other);
        LangManager.get().send(Lang3945.AUTH_CONFIRMED, chefPlayer, Map.of("%cell%", other.getName()));
        Bukkit.getLogger().info("[UHC 39-45] Auth AUTHENTICATED " + own.getName() + " ↔ " + other.getName()
                + " via " + chefPlayer.getName());
        return true;
    }

    public boolean spoof(Player player, String rawSignal) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return false;
        }
        UHCPlayer up = UHCPlayerManager.get().getPlayer(player);
        if (up == null) {
            return false;
        }
        Role role = scenario.getRoleByUHCPlayer(up);
        if (!(role instanceof Infiltre)) {
            LangManager.get().send(Lang3945.INFIL_NOT_ROLE, player);
            return false;
        }
        String signal = AuthCodes.normalize(rawSignal);
        if (signal.isEmpty()) {
            LangManager.get().send(Lang3945.INFIL_SPOOF_USAGE, player);
            return false;
        }
        PlayerKnowledge knowledge = scenario.knowledge().of(up.getUuid());
        if (!knowledge.hasIntercepted(signal)) {
            LangManager.get().send(Lang3945.INFIL_SPOOF_NO_FRAGMENT, player);
            return false;
        }
        ResistanceCell target = scenario.cells().bySignal(signal);
        if (target == null) {
            LangManager.get().send(Lang3945.AUTH_UNKNOWN, player);
            return false;
        }
        knowledge.addCell(target.getId());
        knowledge.setAuth(target.getId(), AuthState.SUSPECTED);
        LangManager.get().send(Lang3945.INFIL_SPOOF_OK, player, Map.of("%cell%", target.getName()));
        notifyCell(target, Lang3945.INFIL_SPOOF_NOTIFY, null);
        Bukkit.getLogger().info("[UHC 39-45] Infiltré usurpe le signal de " + target.getName()
                + " (pas de roster)");
        return true;
    }

    public void showCode(Player player) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return;
        }
        UHCPlayer up = UHCPlayerManager.get().getPlayer(player);
        if (up == null) {
            return;
        }
        ResistanceCell cell = scenario.cells().cellOf(up.getUuid());
        if (cell == null) {
            LangManager.get().send(Lang3945.AUTH_NOT_RESISTANCE, player);
            return;
        }
        LangManager.get().send(cell.isIsolate() ? Lang3945.CODE_ISOLATE : Lang3945.CODE_SIGNAL, player,
                Map.of("%cell%", cell.getName(), "%signal%", cell.getSignal() == null ? "?" : cell.getSignal()));
        if (canSeeSecret(up, cell)) {
            LangManager.get().send(Lang3945.CODE_SECRET, player,
                    Map.of("%secret%", cell.getSecret() == null ? "?" : cell.getSecret()));
        } else {
            LangManager.get().send(Lang3945.CODE_SECRET_HIDDEN, player);
        }
    }

    private void completeLink(Scenario3945 scenario, ResistanceCell a, ResistanceCell b) {
        if (a == null || b == null) {
            return;
        }
        KnowledgeService knowledge = scenario.knowledge();
        for (UUID member : a.getMembers()) {
            Role role = roleOf(scenario, member);
            knowledge.grantAuthenticatedNetwork(member, b, role);
            Player p = bukkit(member);
            if (p != null) {
                LangManager.get().send(Lang3945.AUTH_LINKED, p, Map.of(
                        "%cell%", b.getName(),
                        "%members%", memberNames(b, member)));
            }
        }
        for (UUID member : b.getMembers()) {
            Role role = roleOf(scenario, member);
            knowledge.grantAuthenticatedNetwork(member, a, role);
            Player p = bukkit(member);
            if (p != null) {
                LangManager.get().send(Lang3945.AUTH_LINKED, p, Map.of(
                        "%cell%", a.getName(),
                        "%members%", memberNames(a, member)));
            }
        }
    }

    private boolean canConfirm(UHCPlayer player, ResistanceCell cell, Scenario3945 scenario) {
        UUID chef = livingChef(cell);
        if (chef == null) {
            return cell.contains(player.getUuid());
        }
        if (chef.equals(player.getUuid())) {
            return true;
        }
        Role role = scenario.getRoleByUHCPlayer(player);
        return role instanceof ChefReseau;
    }

    private UUID livingChef(ResistanceCell cell) {
        if (cell.getChefId() == null) {
            return null;
        }
        UHCPlayer chef = UHCPlayerManager.get().getPlayer(cell.getChefId());
        return chef != null && chef.isStillInGame() ? cell.getChefId() : null;
    }

    private void leakIntercept(Scenario3945 scenario, String signal, boolean matched) {
        if (!matched || signal == null || signal.isEmpty()) {
            return;
        }
        for (Map.Entry<UHCPlayer, Role> entry : scenario.getPlayersRoles().entrySet()) {
            if (!(entry.getValue() instanceof Infiltre)) {
                continue;
            }
            UUID id = entry.getKey().getUuid();
            if (!isListening(id)) {
                continue;
            }
            scenario.knowledge().of(id).addInterceptedSignal(signal);
            Player listener = entry.getKey().getPlayer();
            if (listener != null && listener.isOnline()) {
                LangManager.get().send(Lang3945.INFIL_INTERCEPT_HIT, listener, Map.of("%signal%", signal));
            }
            Bukkit.getLogger().info("[UHC 39-45] Infiltré intercepte le fragment " + signal);
        }
    }

    private void notifyCell(ResistanceCell cell, Lang3945 key, String playerName) {
        Map<String, Object> vars = playerName == null
                ? Map.of()
                : Map.of("%player%", playerName);
        for (UUID member : cell.getMembers()) {
            Player p = bukkit(member);
            if (p != null) {
                LangManager.get().send(key, p, vars);
            }
        }
    }

    private static Role roleOf(Scenario3945 scenario, UUID uuid) {
        UHCPlayer up = UHCPlayerManager.get().getPlayer(uuid);
        return up == null ? null : scenario.getRoleByUHCPlayer(up);
    }

    private static Player bukkit(UUID uuid) {
        UHCPlayer up = UHCPlayerManager.get().getPlayer(uuid);
        Player player = up == null ? null : up.getPlayer();
        return player != null && player.isOnline() ? player : null;
    }

    private static String memberNames(ResistanceCell cell, UUID except) {
        StringBuilder builder = new StringBuilder();
        for (UUID member : cell.getMembers()) {
            if (member.equals(except)) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(KnowledgeService.displayName(member));
        }
        return builder.length() == 0 ? "—" : builder.toString();
    }
}
