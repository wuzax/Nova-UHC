package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import net.novaproject.uhc3945.roles.Commandant;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OfficierRapportCommand extends CommandAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel le rapport est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 2;

    public OfficierRapportCommand() {
        setCooldown(120);
        setMaxUse(-1);
    }

    @Override
    public String getCommandKey() {
        return "rapport";
    }

    @Override
    public String getName() {
        return "Rapport";
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_OFFICIER_RAPPORT);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;

        Commandant commandant = Power3945.findAliveRole(Commandant.class);
        if (commandant == null) {
            Power3945.send(player, Lang3945.MSG_OFFICIER_RAPPORT_DEAD);
            Fx.Sounds.fail(player);
            return true;
        }

        Player target = Power3945.livingOwner(commandant);
        if (target == null) {
            Power3945.send(player, Lang3945.MSG_OFFICIER_RAPPORT_UNKNOWN);
            Fx.Sounds.fail(player);
            return true;
        }
        if (!player.getWorld().equals(target.getWorld())) {
            Power3945.send(player, Lang3945.MSG_OFFICIER_RAPPORT_WORLD);
            Fx.Sounds.success(player);
            return true;
        }

        double dist = player.getLocation().distance(target.getLocation());
        Power3945.send(player, Lang3945.MSG_OFFICIER_RAPPORT, Map.of(
                "%distance%", Power3945.approxDistance(dist),
                "%dir%", Power3945.cardinal(player.getLocation(), target.getLocation())));
        Fx.Sounds.success(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }
}
