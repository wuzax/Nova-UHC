package net.novaproject.uhc3945;

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
                lm.register(Lang3945.values());
                lm.importShipped(Main.this);
                lm.requestReload();
                ScenarioManager.get().addScenario(new Scenario3945());
                getLogger().info("UHC 39-45 : scénario enregistré.");
            }
        }.runTaskLater(this, 20);
    }
}
