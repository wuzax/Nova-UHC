package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.scenario.role.CampWinPolicy;
import net.novaproject.novauhc.scenario.role.ModeKit;
import net.novaproject.novauhc.scenario.role.ScenarioRole;
import net.novaproject.novauhc.scenario.role.camps.Camps;
import net.novaproject.novauhc.utils.item.ItemCreator;
import net.novaproject.uhc3945.roles.ChefReseau;
import net.novaproject.uhc3945.roles.CivilNeutre;
import net.novaproject.uhc3945.roles.Commandant;
import net.novaproject.uhc3945.roles.Informateur;
import net.novaproject.uhc3945.roles.Resistant;
import net.novaproject.uhc3945.roles.Soldat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Scenario3945 extends ScenarioRole<Role3945> {

    private static Scenario3945 instance;

    public static Scenario3945 get() {
        return instance;
    }

    @Override
    public Camps[] getCamps() {
        return Camps3945.values();
    }

    @Override
    public String getName() {
        return "UHC 39-45";
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(Lang3945.SCENARIO_DESC, player);
    }

    @Override
    public ItemCreator getItem() {
        return new ItemCreator(Material.MAP).setName("§8§lUHC 39-45");
    }

    @Override
    public String getColor() {
        return "§8";
    }

    @Override
    public String getPrefix() {
        return "§8§l39-45 §8│";
    }

    @Override
    public void setup() {
        super.setup();
        instance = this;
        // TODO: cellules Résistance, infiltration, limites de groupes, pouvoirs, worldgen
        ModeKit.of(this)
                .camp(Camps3945.AXE, CampWinPolicy::together)
                .camp(Camps3945.RESISTANCE, CampWinPolicy::together)
                .camp(Camps3945.CIVILIAN, CampWinPolicy::solo)
                .uniqueRoles(Commandant.class, ChefReseau.class, Informateur.class)
                .role(Soldat.class)
                .role(Resistant.class)
                .filler(CivilNeutre.class)
                .revealWithin(Camps3945.AXE)
                .apply();
        Bukkit.getLogger().info("[UHC 39-45] Scénario initialisé (camps + rôles stub).");
    }
}
