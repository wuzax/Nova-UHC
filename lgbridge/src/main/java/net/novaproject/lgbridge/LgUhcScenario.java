package net.novaproject.lgbridge;

import net.novaproject.novauhc.event.UhcGameEvents.UhcGameStartEvent;
import net.novaproject.novauhc.scenario.Scenario;
import net.novaproject.novauhc.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LgUhcScenario extends Scenario implements Listener {

    @Override
    public String getName() {
        return "LG-UHC (Ph1Lou)";
    }

    @Override
    public String getDescription(Player player) {
        String server = Main.get() == null ? "lg" : Main.get().settings().serverName();
        return "§5LG-UHC §7n'est pas un port de rôles Nova. "
                + "Activer ce mode (ou lancer la partie) envoie §ftous les joueurs connectés §7"
                + "vers le backend Velocity/Bungee §d" + server
                + " §7via §fBungeeCord/Connect§7. "
                + "Le Loup-Garou se joue sur l'instance Ph1Lou WereWolfPlugin, pas ici.";
    }

    @Override
    public ItemCreator getItem() {
        return new ItemCreator(Material.SKULL_ITEM)
                .setDurability((short) 1)
                .setName("§5§lLG-UHC (Ph1Lou)");
    }

    @Override
    public String getColor() {
        return "§5";
    }

    @Override
    public String getPrefix() {
        return "§5§lLG-UHC §8│";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public void toggleActive() {
        super.toggleActive();
        Main plugin = Main.get();
        if (plugin == null) {
            return;
        }
        if (!isActive()) {
            plugin.getServer().broadcastMessage(plugin.settings().disabledMessage());
            return;
        }
        if (plugin.settings().transferOnSelect()) {
            plugin.transferAll("select");
        }
    }

    @Override
    public void onGameStart() {
        Main plugin = Main.get();
        if (plugin == null || !plugin.settings().transferOnStart()) {
            return;
        }
        plugin.transferAll("start");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onNovaGameStart(UhcGameStartEvent event) {
        if (!isActive()) {
            return;
        }
        Main plugin = Main.get();
        if (plugin == null || !plugin.settings().transferOnStart()) {
            return;
        }
        event.setCancelled(true);
        plugin.transferAll("start");
    }

    @EventHandler
    public void onJoinWhileLgSelected(PlayerJoinEvent event) {
        if (!isActive()) {
            return;
        }
        Main plugin = Main.get();
        if (plugin == null) {
            return;
        }
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!isActive() || player == null || !player.isOnline()) {
                return;
            }
            String whisper = plugin.settings().transferMessage();
            if (!whisper.isEmpty()) {
                player.sendMessage(whisper);
            }
            player.sendPluginMessage(
                    plugin,
                    BungeeConnect.CHANNEL,
                    BungeeConnect.connectPayload(plugin.settings().serverName()));
        }, 20L);
    }
}
