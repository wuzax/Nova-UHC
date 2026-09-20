package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.template.UseAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import net.novaproject.uhc3945.roles.Commandant;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SoldatChargeActive extends UseAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel la charge est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 2;

    @Var(name = "Durée (s)", desc = "Durée avec Commandant vivant.", type = VariableType.TIME, min = 3, max = 12)
    private int duration = 6;

    @Var(name = "Durée sans Commandant (s)", desc = "Durée si le Commandant est hors combat.", type = VariableType.TIME, min = 2, max = 8)
    private int weakDuration = 4;

    public SoldatChargeActive() {
        setCooldown(240);
        setMaxUse(-1);
    }

    @Override
    public String getName() {
        return "Charge";
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_POWDER;
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_SOLDAT_CHARGE);
    }

    @Override
    public boolean onEnable(Player player) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;

        boolean commandantAlive = Power3945.findAliveRole(Commandant.class) != null;
        int seconds = commandantAlive ? duration : weakDuration;
        int ticks = seconds * 20;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ticks, commandantAlive ? 1 : 0), true);
        if (commandantAlive) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ticks, 0), true);
            Power3945.send(player, Lang3945.MSG_SOLDAT_CHARGE);
        } else {
            Power3945.send(player, Lang3945.MSG_SOLDAT_CHARGE_WEAK);
        }
        Fx.Sounds.success(player);
        return true;
    }
}
