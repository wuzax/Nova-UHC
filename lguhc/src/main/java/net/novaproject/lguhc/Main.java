package net.novaproject.lguhc;

import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.scenario.ScenarioManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                LangManager lm = LangManager.get();
                lm.register(LangLguhc.values());
                lm.importShipped(Main.this);
                lm.requestReload();
                ScenarioManager.get().addScenario(new ScenarioLguhc());
                getLogger().info("LG-UHC : scénario Nova enregistré (rôles ModeKit, pas WereWolfPlugin 1.21).");
            }
        }.runTaskLater(this, 20);
    }
}
