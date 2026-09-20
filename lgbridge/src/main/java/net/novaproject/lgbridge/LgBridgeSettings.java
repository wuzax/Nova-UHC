package net.novaproject.lgbridge;

import org.bukkit.configuration.file.FileConfiguration;

public final class LgBridgeSettings {

    private final String serverName;
    private final boolean transferOnSelect;
    private final boolean transferOnStart;
    private final String prefix;
    private final String selected;
    private final String starting;
    private final String transfer;
    private final String empty;
    private final String disabled;
    private final String reloaded;

    public LgBridgeSettings(
            String serverName,
            boolean transferOnSelect,
            boolean transferOnStart,
            String prefix,
            String selected,
            String starting,
            String transfer,
            String empty,
            String disabled,
            String reloaded) {
        this.serverName = BungeeConnect.requireServerName(serverName);
        this.transferOnSelect = transferOnSelect;
        this.transferOnStart = transferOnStart;
        this.prefix = prefix == null ? "" : prefix;
        this.selected = selected == null ? "" : selected;
        this.starting = starting == null ? "" : starting;
        this.transfer = transfer == null ? "" : transfer;
        this.empty = empty == null ? "" : empty;
        this.disabled = disabled == null ? "" : disabled;
        this.reloaded = reloaded == null ? "" : reloaded;
    }

    public static LgBridgeSettings from(FileConfiguration config) {
        return new LgBridgeSettings(
                config.getString("lg-server-name", "lg"),
                config.getBoolean("transfer-on-select", true),
                config.getBoolean("transfer-on-start", true),
                config.getString("messages.prefix", "§5§lLG-UHC §8│"),
                config.getString("messages.selected", "%prefix% §fTransfert vers §d%server%§f…"),
                config.getString("messages.starting", "%prefix% §fTransfert vers §d%server%§f."),
                config.getString("messages.transfer", "%prefix% §7Connexion vers §d%server%§7…"),
                config.getString("messages.empty", "%prefix% §cAucun joueur en ligne à transférer."),
                config.getString("messages.disabled", "%prefix% §7Mode LG-UHC désactivé."),
                config.getString("messages.reloaded", "%prefix% §aConfiguration rechargée. Serveur cible : §d%server%§a."));
    }

    public String serverName() {
        return serverName;
    }

    public boolean transferOnSelect() {
        return transferOnSelect;
    }

    public boolean transferOnStart() {
        return transferOnStart;
    }

    public String selectedMessage() {
        return format(selected);
    }

    public String startingMessage() {
        return format(starting);
    }

    public String transferMessage() {
        return format(transfer);
    }

    public String emptyMessage() {
        return format(empty);
    }

    public String disabledMessage() {
        return format(disabled);
    }

    public String reloadedMessage() {
        return format(reloaded);
    }

    String format(String template) {
        if (template == null || template.isEmpty()) {
            return "";
        }
        return template.replace("%prefix%", prefix).replace("%server%", serverName);
    }
}
