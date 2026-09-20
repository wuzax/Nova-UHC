package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.template.UseAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.ability.toolbox.TargetSelector;
import net.novaproject.novauhc.game.EpisodeManager;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CivilInstinctActive extends UseAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel l'instinct est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 3;

    @Var(name = "Rayon", desc = "Rayon de détection, sans révéler d'identité.", type = VariableType.DOUBLE, min = 15, max = 80)
    private double radius = 40;

    public CivilInstinctActive() {
        setCooldown(0);
        setMaxUse(-1);
    }

    @Override
    public String getName() {
        return "Instinct";
    }

    @Override
    public Material getMaterial() {
        return Material.WATCH;
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_CIVIL_INSTINCT);
    }

    @Override
    public boolean onEnable(Player player) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;
        if (!EpisodeManager.EpisodeLimiter.tryUse(player.getUniqueId(), "uhc3945-instinct")) {
            Power3945.send(player, Lang3945.MSG_CIVIL_INSTINCT_USED);
            Fx.Sounds.fail(player);
            return false;
        }

        boolean present = !TargetSelector.playersInRadius(player, radius).isEmpty();
        Power3945.send(player, present ? Lang3945.MSG_CIVIL_INSTINCT_YES : Lang3945.MSG_CIVIL_INSTINCT_NO);
        if (present) Fx.Sounds.pulse(player);
        else Fx.Sounds.success(player);
        return true;
    }
}
