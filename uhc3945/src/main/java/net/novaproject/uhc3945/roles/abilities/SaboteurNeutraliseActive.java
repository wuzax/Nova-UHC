package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.AbilitySuppression;
import net.novaproject.novauhc.ability.template.UseAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class SaboteurNeutraliseActive extends UseAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel le sabotage est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 4;

    @Var(name = "Portée", desc = "Distance maximale de la cible visée.", type = VariableType.DOUBLE, min = 4, max = 16)
    private double range = 8;

    @Var(name = "Durée (s)", desc = "Durée du silence des pouvoirs.", type = VariableType.TIME, min = 6, max = 25)
    private int duration = 12;

    public SaboteurNeutraliseActive() {
        setCooldown(360);
        setMaxUse(2);
    }

    @Override
    public String getName() {
        return "Sabotage";
    }

    @Override
    public Material getMaterial() {
        return Material.REDSTONE;
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_SABOTEUR);
    }

    @Override
    public boolean onEnable(Player player) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;

        Player target = Power3945.requireLookedAt(player, range);
        if (target == null) return false;

        UHCPlayer uhcTarget = UHCPlayerManager.get().getPlayer(target);
        if (uhcTarget != null) {
            AbilitySuppression.suppress(uhcTarget, duration, false);
        }
        target.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        target.removePotionEffect(PotionEffectType.SPEED);
        target.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        target.removePotionEffect(PotionEffectType.INVISIBILITY);
        target.removePotionEffect(PotionEffectType.REGENERATION);

        Power3945.send(player, Lang3945.MSG_SABOTEUR, Map.of("%target%", target.getName()));
        Power3945.send(target, Lang3945.MSG_SABOTEUR_TARGET);
        Fx.Sounds.success(player);
        Fx.Sounds.fail(target);
        return true;
    }
}
