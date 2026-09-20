package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.reveal.KPIBuilder;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import org.bukkit.entity.Player;

import java.util.Map;

public class InformateurTransmettreCommand extends CommandAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel la transmission est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 3;

    public InformateurTransmettreCommand() {
        setCooldown(60);
        setMaxUse(-1);
    }

    @Override
    public String getCommandKey() {
        return "transmettre";
    }

    @Override
    public String getName() {
        return "Transmettre";
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_INFORMATEUR_TRANSMETTRE);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;
        if (args.length == 0) {
            Power3945.send(player, Lang3945.POWER_USAGE, Map.of("%usage%", "/role transmettre <joueur>"));
            Fx.Sounds.fail(player);
            return false;
        }

        Player target = Power3945.requireNamedPlaying(player, args[0]);
        if (target == null) return false;

        String report = lastReportOf(player);
        if (report == null || report.isBlank()) {
            Power3945.send(player, Lang3945.MSG_INFORMATEUR_NO_REPORT);
            Fx.Sounds.fail(player);
            return false;
        }

        Power3945.send(target, Lang3945.MSG_INFORMATEUR_TRANSMETTRE_RECEIVE, Map.of("%report%", report));
        Power3945.send(player, Lang3945.MSG_INFORMATEUR_TRANSMETTRE, Map.of("%target%", target.getName()));
        Fx.Sounds.success(player);
        Fx.Sounds.pulse(target);
        return true;
    }

    private String lastReportOf(Player player) {
        UHCPlayer up = getUHCPlayer(player);
        Role role = up == null ? null : KPIBuilder.roleOf(up);
        if (role == null) return null;
        for (Ability ability : role.getAbilities()) {
            if (ability instanceof InformateurAnalyseCommand analyse) {
                return analyse.getLastReport();
            }
        }
        return null;
    }
}
