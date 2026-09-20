package net.novaproject.lguhc;

import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.reveal.KPIBuilder;
import org.bukkit.entity.Player;

import java.util.Map;

public final class LguhcPower {

    public static final String WOLF_CHAT_ID = "lguhc-wolves";
    public static final String WOLF_CHAT_PREFIX = "lg";

    private LguhcPower() {
    }

    public static Role roleOf(Player player) {
        if (player == null) return null;
        UHCPlayer up = UHCPlayerManager.get().getPlayer(player);
        return up == null ? null : KPIBuilder.roleOf(up);
    }

    public static Player requireNamedPlaying(Player viewer, String name) {
        return findNamedPlaying(viewer, name, false);
    }

    public static Player requireNamedPlayingAllowSelf(Player viewer, String name) {
        return findNamedPlaying(viewer, name, true);
    }

    private static Player findNamedPlaying(Player viewer, String name, boolean allowSelf) {
        if (name == null || name.isBlank()) {
            send(viewer, LangLguhc.POWER_PLAYER_NOT_FOUND);
            Fx.Sounds.fail(viewer);
            return null;
        }
        for (UHCPlayer up : UHCPlayerManager.get().getPlayingOnlineUHCPlayers()) {
            Player other = up.getPlayer();
            if (other == null || !other.isOnline()) continue;
            if (!allowSelf && other.equals(viewer)) continue;
            if (other.getName().equalsIgnoreCase(name)) return other;
        }
        send(viewer, LangLguhc.POWER_PLAYER_NOT_FOUND);
        Fx.Sounds.fail(viewer);
        return null;
    }

    public static void send(Player player, LangLguhc key) {
        LangManager.get().send(key, player);
    }

    public static void send(Player player, LangLguhc key, Map<String, Object> extra) {
        LangManager.get().send(key, player, extra);
    }

    public static String text(Player player, LangLguhc key) {
        return LangManager.get().get(key, player);
    }
}
