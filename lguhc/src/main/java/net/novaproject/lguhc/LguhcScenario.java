package net.novaproject.lguhc;

import net.novaproject.novauhc.event.UhcGameEvents.UhcGameStartEvent;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.scenario.Scenario;
import net.novaproject.novauhc.scenario.ScenarioManager;
import net.novaproject.novauhc.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LguhcScenario extends Scenario implements Listener {

    public static final String NAME = "LG-UHC (Ph1Lou)";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(LangLguhc.SCENARIO_DESC, player);
    }

    @Override
    public ItemCreator getItem() {
        return new ItemCreator(Material.BONE).setName("§c§lLG-UHC");
    }

    @Override
    public String getColor() {
        return "§c";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean overridesVictory() {
        return isActive() && WereWolfPresence.available();
    }

    @Override
    public void toggleActive() {
        super.toggleActive();
        if (!isActive()) {
            return;
        }
        for (Scenario other : ScenarioManager.get().getSpecialScenarios()) {
            if (other != this && other.isActive()) {
                other.toggleActive();
            }
        }
        if (WereWolfPresence.available()) {
            LangManager.get().sendAll(LangLguhc.DETECTED);
            Bukkit.getLogger().info("[LG-UHC] WereWolfPlugin / GetWereWolfAPI détecté. Lancer via /a, pas Nova.");
        } else {
            LangManager.get().sendAll(LangLguhc.MISSING_PLUGIN);
            Bukkit.getLogger().warning("[LG-UHC] WereWolfPlugin absent. Voir LGUHC.md (Spigot #73113).");
        }
    }

    @EventHandler
    public void onNovaStart(UhcGameStartEvent event) {
        if (!isActive()) {
            return;
        }
        if (event.isForced()) {
            Bukkit.getLogger().severe("[LG-UHC] Start Nova forcé alors que LG-UHC est actif — deux moteurs UHC.");
            return;
        }
        event.setCancelled(true);
        LangManager.get().sendAll(LangLguhc.BLOCK_NOVA_START);
    }
}
