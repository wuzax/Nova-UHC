package net.novaproject.lgbridge;

import net.novaproject.novauhc.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;
    private LgBridgeSettings settings;

    public static Main get() {
        return instance;
    }

    public LgBridgeSettings settings() {
        return settings;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadSettings();
        Messenger messenger = getServer().getMessenger();
        if (!messenger.isOutgoingChannelRegistered(this, BungeeConnect.CHANNEL)) {
            messenger.registerOutgoingPluginChannel(this, BungeeConnect.CHANNEL);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                ScenarioManager.get().addScenario(new LgUhcScenario());
                getLogger().info("LG-UHC (Ph1Lou) : mode de jeu enregistré (transfert vers "
                        + settings.serverName() + ").");
            }
        }.runTaskLater(this, 20);
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, BungeeConnect.CHANNEL);
        instance = null;
    }

    public void reloadSettings() {
        reloadConfig();
        settings = LgBridgeSettings.from(getConfig());
    }

    public int transferAll(String reason) {
        LgBridgeSettings current = settings;
        String broadcast = "start".equals(reason) ? current.startingMessage() : current.selectedMessage();
        if (!broadcast.isEmpty()) {
            Bukkit.broadcastMessage(broadcast);
        }
        List<Player> online = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (online.isEmpty()) {
            Bukkit.broadcastMessage(current.emptyMessage());
            getLogger().warning("Transfert LG ignoré : aucun joueur en ligne.");
            return 0;
        }
        byte[] payload = BungeeConnect.connectPayload(current.serverName());
        String whisper = current.transferMessage();
        int sent = 0;
        for (Player player : online) {
            if (player == null || !player.isOnline()) {
                continue;
            }
            if (!whisper.isEmpty()) {
                player.sendMessage(whisper);
            }
            player.sendPluginMessage(this, BungeeConnect.CHANNEL, payload);
            sent++;
        }
        getLogger().info("BungeeCord Connect → " + current.serverName()
                + " (" + sent + " joueur(s), motif=" + reason + ").");
        return sent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!"lgbridge".equalsIgnoreCase(command.getName())) {
            return false;
        }
        if (!sender.hasPermission("novauhc.host")) {
            sender.sendMessage("§cPermission refusée.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§7/" + label + " reload §8| §7/" + label + " transfer");
            return true;
        }
        if ("reload".equalsIgnoreCase(args[0])) {
            reloadSettings();
            sender.sendMessage(settings.reloadedMessage());
            return true;
        }
        if ("transfer".equalsIgnoreCase(args[0])) {
            transferAll("command");
            return true;
        }
        sender.sendMessage("§7/" + label + " reload §8| §7/" + label + " transfer");
        return true;
    }
}
