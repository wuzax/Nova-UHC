package net.novaproject.uhc3945;

import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.ability.toolbox.TargetSelector;
import net.novaproject.novauhc.game.EpisodeManager;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.reveal.KPIBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public final class Power3945 {

    private static final String[] CARDINALS = {
            "sud", "sud-ouest", "ouest", "nord-ouest",
            "nord", "nord-est", "est", "sud-est"
    };

    private Power3945() {
    }

    public static int episode() {
        if (!EpisodeManager.get().isEnabled()) return Integer.MAX_VALUE;
        return EpisodeManager.get().getEpisode();
    }

    public static boolean denyIfTooEarly(Player player, int minEpisode) {
        if (episode() >= minEpisode) return false;
        send(player, Lang3945.POWER_LOCKED_EPISODE, Map.of("%episode%", minEpisode));
        Fx.Sounds.fail(player);
        return true;
    }

    public static Role roleOf(Player player) {
        if (player == null) return null;
        UHCPlayer up = UHCPlayerManager.get().getPlayer(player);
        return up == null ? null : KPIBuilder.roleOf(up);
    }

    public static boolean sameCamp(Player player, Camps3945 camp) {
        Role role = roleOf(player);
        return role != null && role.getCamp() != null && role.getCamp().is(camp);
    }

    public static <T extends Role> T findAliveRole(Class<T> type) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) return null;
        for (Map.Entry<UHCPlayer, Role3945> entry : scenario.getPlayersRoles().entrySet()) {
            if (!type.isInstance(entry.getValue())) continue;
            UHCPlayer owner = entry.getKey();
            if (owner == null || !owner.isStillInGame()) continue;
            return type.cast(entry.getValue());
        }
        return null;
    }

    public static Player livingOwner(Role role) {
        if (role == null || role.getOwner() == null) return null;
        Player player = role.getOwner().getPlayer();
        if (player == null || !player.isOnline() || player.isDead()) return null;
        return player;
    }

    public static Player requireLookedAt(Player owner, double maxDistance) {
        Player target = TargetSelector.lookedAt(owner, maxDistance);
        if (target != null) return target;
        send(owner, Lang3945.POWER_NO_TARGET);
        Fx.Sounds.fail(owner);
        return null;
    }

    public static Player requireNamedPlaying(Player viewer, String name) {
        if (name == null || name.isBlank()) {
            send(viewer, Lang3945.POWER_PLAYER_NOT_FOUND);
            Fx.Sounds.fail(viewer);
            return null;
        }
        for (UHCPlayer up : UHCPlayerManager.get().getPlayingOnlineUHCPlayers()) {
            Player other = up.getPlayer();
            if (other == null || !other.isOnline() || other.equals(viewer)) continue;
            if (other.getName().equalsIgnoreCase(name)) return other;
        }
        send(viewer, Lang3945.POWER_PLAYER_NOT_FOUND);
        Fx.Sounds.fail(viewer);
        return null;
    }

    public static boolean denyIfOutOfRange(Player viewer, Player target, double maxDistance) {
        if (target == null) return true;
        if (viewer.getWorld().equals(target.getWorld())
                && viewer.getLocation().distanceSquared(target.getLocation()) <= maxDistance * maxDistance) {
            return false;
        }
        send(viewer, Lang3945.POWER_OUT_OF_RANGE, Map.of("%range%", (int) maxDistance));
        Fx.Sounds.fail(viewer);
        return true;
    }

    public static String cardinal(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double angle = Math.toDegrees(Math.atan2(-dx, dz));
        int index = ((int) Math.round(angle / 45.0)) & 7;
        return CARDINALS[index];
    }

    public static String approxDistance(double dist) {
        if (dist < 15) return "très proche";
        int rounded = (int) (Math.round(dist / 20.0) * 20);
        return "~" + Math.max(20, rounded) + " blocs";
    }

    public static String healthBand(Player player) {
        double ratio = player.getMaxHealth() <= 0 ? 1 : player.getHealth() / player.getMaxHealth();
        if (ratio >= 0.8) return "en forme";
        if (ratio >= 0.4) return "blessé";
        return "critique";
    }

    public static String gearBand(Player player) {
        int pieces = 0;
        for (org.bukkit.inventory.ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item.getType() != org.bukkit.Material.AIR) pieces++;
        }
        if (pieces >= 3) return "lourd";
        if (pieces >= 1) return "moyen";
        return "léger";
    }

    public static void send(Player player, Lang3945 key) {
        LangManager.get().send(key, player);
    }

    public static void send(Player player, Lang3945 key, Map<String, Object> extra) {
        LangManager.get().send(key, player, extra);
    }

    public static String text(Player player, Lang3945 key) {
        return LangManager.get().get(key, player);
    }
}
