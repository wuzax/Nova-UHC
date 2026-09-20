package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.template.UseAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.ability.toolbox.TargetSelector;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ChefSignalActive extends UseAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel le signal est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 3;

    @Var(name = "Rayon", desc = "Rayon de détection, sans révéler d'identités.", type = VariableType.DOUBLE, min = 20, max = 150)
    private double radius = 80;

    public ChefSignalActive() {
        setCooldown(240);
        setMaxUse(-1);
    }

    @Override
    public String getName() {
        return "Signal";
    }

    @Override
    public Material getMaterial() {
        return Material.EYE_OF_ENDER;
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_CHEF_SIGNAL);
    }

    @Override
    public boolean onEnable(Player player) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;

        List<Player> nearby = TargetSelector.playersInRadius(player, radius);
        if (nearby.isEmpty()) {
            Power3945.send(player, Lang3945.MSG_CHEF_SIGNAL_NONE);
            Fx.Sounds.pulse(player);
            return true;
        }

        Player closest = TargetSelector.nearest(player, radius);
        if (closest == null) {
            Power3945.send(player, Lang3945.MSG_CHEF_SIGNAL_NONE);
            return true;
        }
        double dist = player.getLocation().distance(closest.getLocation());
        Power3945.send(player, Lang3945.MSG_CHEF_SIGNAL, Map.of(
                "%count%", nearby.size(),
                "%distance%", Power3945.approxDistance(dist),
                "%dir%", Power3945.cardinal(player.getLocation(), closest.getLocation())));
        Fx.Sounds.success(player);
        return true;
    }
}
