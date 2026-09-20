package net.novaproject.lguhc.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.lguhc.CampsLguhc;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.RoleLguhc;
import net.novaproject.lguhc.roles.abilities.ChasseurShotPassive;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;

public class Chasseur extends RoleLguhc {

    @Var(name = "Tir", desc = "Tue l'assassin à la mort du Chasseur.", type = VariableType.ABILITY)
    public Ability shot;

    public Chasseur() {
        setCamp(CampsLguhc.VILLAGE);
        setStrength(0);
        setResistance(0);
        this.shot = new ChasseurShotPassive();
    }

    @Override
    public String getName() {
        return "Chasseur";
    }

    @Override
    public Material getIconMaterial() {
        return Material.BOW;
    }

    @Override
    public Lang getDescriptionLang() {
        return LangLguhc.ROLE_DESC_CHASSEUR;
    }

    @Override
    public void onDeath(UHCPlayer uhcPlayer, UHCPlayer killer, PlayerDeathEvent event) {
        super.onDeath(uhcPlayer, killer, event);
        if (uhcPlayer != getOwner()) return;
        if (killer == null || !killer.isPlaying()) return;
        Player target = killer.getPlayer();
        if (target == null || !target.isOnline()) return;
        LangManager.get().sendAll(LangLguhc.MSG_CHASSEUR_SHOT, Map.of("%target%", target.getName()));
        target.setHealth(0);
    }
}
