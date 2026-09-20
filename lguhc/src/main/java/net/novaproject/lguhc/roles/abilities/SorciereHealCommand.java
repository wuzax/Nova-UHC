package net.novaproject.lguhc.roles.abilities;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.LguhcPower;
import org.bukkit.entity.Player;

import java.util.Map;

public class SorciereHealCommand extends CommandAbility {

    public SorciereHealCommand() {
        setCooldown(0);
        setMaxUse(1);
    }

    @Override
    public String getCommandKey() {
        return "soigner";
    }

    @Override
    public String getName() {
        return "Soigner";
    }

    @Override
    public String getDescription(Player player) {
        return LguhcPower.text(player, LangLguhc.ABILITY_SOIGNER);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length == 0) {
            LguhcPower.send(player, LangLguhc.POWER_USAGE, Map.of("%usage%", "/role soigner <joueur>"));
            Fx.Sounds.fail(player);
            return false;
        }
        Player target = LguhcPower.requireNamedPlayingAllowSelf(player, args[0]);
        if (target == null) return false;
        target.setHealth(target.getMaxHealth());
        LguhcPower.send(player, LangLguhc.MSG_SORCIERE_HEAL, Map.of("%target%", target.getName()));
        Fx.Sounds.success(player);
        Fx.Sounds.pulse(target);
        return true;
    }
}
