package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import org.bukkit.entity.Player;

import java.util.Map;

public class InformateurAnalyseCommand extends CommandAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel l'analyse est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 2;

    @Var(name = "Portée", desc = "Distance maximale pour analyser. 0 = illimitée.", type = VariableType.DOUBLE, min = 0, max = 200)
    private double range = 80;

    private String lastReport;

    public InformateurAnalyseCommand() {
        setCooldown(180);
        setMaxUse(-1);
    }

    @Override
    public String getCommandKey() {
        return "analyser";
    }

    @Override
    public String getName() {
        return "Analyser";
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_INFORMATEUR_ANALYSE);
    }

    public String getLastReport() {
        return lastReport;
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;
        if (args.length == 0) {
            Power3945.send(player, Lang3945.POWER_USAGE, Map.of("%usage%", "/role analyser <joueur>"));
            Fx.Sounds.fail(player);
            return false;
        }

        Player target = Power3945.requireNamedPlaying(player, args[0]);
        if (target == null) return false;
        if (range > 0 && Power3945.denyIfOutOfRange(player, target, range)) return false;

        String distance;
        if (!player.getWorld().equals(target.getWorld())) {
            distance = "autre dimension";
        } else {
            distance = Power3945.approxDistance(player.getLocation().distance(target.getLocation()));
        }

        lastReport = target.getName() + " · " + Power3945.healthBand(target)
                + " · équipement " + Power3945.gearBand(target)
                + " · " + distance;
        Power3945.send(player, Lang3945.MSG_INFORMATEUR_ANALYSE, Map.of(
                "%target%", target.getName(),
                "%health%", Power3945.healthBand(target),
                "%gear%", Power3945.gearBand(target),
                "%distance%", distance));
        Fx.Sounds.success(player);
        return true;
    }
}
