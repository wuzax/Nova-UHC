package net.novaproject.lguhc.roles.abilities;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.LguhcPower;
import org.bukkit.entity.Player;

import java.util.Map;

public class VoyanteVoirCommand extends CommandAbility {

    public VoyanteVoirCommand() {
        setCooldown(0);
        setMaxUse(1);
    }

    @Override
    public String getCommandKey() {
        return "voir";
    }

    @Override
    public String getName() {
        return "Voir";
    }

    @Override
    public String getDescription(Player player) {
        return LguhcPower.text(player, LangLguhc.ABILITY_VOIR);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length == 0) {
            LguhcPower.send(player, LangLguhc.POWER_USAGE, Map.of("%usage%", "/role voir <joueur>"));
            Fx.Sounds.fail(player);
            return false;
        }
        Player target = LguhcPower.requireNamedPlaying(player, args[0]);
        if (target == null) return false;
        Role role = LguhcPower.roleOf(target);
        String roleName = role == null ? "?" : role.getName();
        LguhcPower.send(player, LangLguhc.MSG_VOYANTE, Map.of("%target%", target.getName(), "%role%", roleName));
        Fx.Sounds.success(player);
        return true;
    }
}
