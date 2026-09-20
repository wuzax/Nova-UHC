package net.novaproject.lguhc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

public final class WereWolfPresence {

    public static final String PLUGIN_NAME = "WereWolfPlugin";
    public static final String API_CLASS = "fr.ph1lou.werewolfapi.GetWereWolfAPI";

    private WereWolfPresence() {
    }

    public static boolean pluginLoaded() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
        return plugin != null && plugin.isEnabled();
    }

    public static boolean apiServicePresent() {
        try {
            Class<?> api = Class.forName(API_CLASS);
            ServicesManager services = Bukkit.getServicesManager();
            RegisteredServiceProvider<?> registration = services.getRegistration(api);
            return registration != null || services.load(api) != null;
        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
            return false;
        }
    }

    public static boolean available() {
        return pluginLoaded() || apiServicePresent();
    }
}
