package net.novaproject.lguhc.roles.abilities;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.LguhcPower;
import org.bukkit.entity.Player;

import java.util.Map;

public class SorciereKillCommand extends CommandAbility {

    public SorciereKillCommand() {
        setCooldown(0);
        setMaxUse(1);
    }

    @Override
    public String getCommandKey() {
        return "tuer";
    }

    @Override
    public String getName() {
        return "Tuer";
    }

    @Override
    public String getDescription(Player player) {
        return LguhcPower.text(player, LangLguhc.ABILITY_TUER);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length == 0) {
            LguhcPower.send(player, LangLguhc.POWER_USAGE, Map.of("%usage%", "/role tuer <joueur>"));
            Fx.Sounds.fail(player);
            return false;
        }
        Player target = LguhcPower.requireNamedPlaying(player, args[0]);
        if (target == null) return false;
        if (target.equals(player)) {
            LguhcPower.send(player, LangLguhc.POWER_SELF);
            Fx.Sounds.fail(player);
            return false;
        }
        LguhcPower.send(player, LangLguhc.MSG_SORCIERE_KILL, Map.of("%target%", target.getName()));
        Fx.Sounds.success(player);
        target.setHealth(0);
        return true;
    }
}
