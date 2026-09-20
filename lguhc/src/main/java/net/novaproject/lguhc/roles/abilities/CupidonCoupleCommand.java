package net.novaproject.lguhc.roles.abilities;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.scenario.role.Bonds;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.LguhcPower;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CupidonCoupleCommand extends CommandAbility {

    private boolean used;

    public CupidonCoupleCommand() {
        setCooldown(0);
        setMaxUse(1);
    }

    @Override
    public String getCommandKey() {
        return "couple";
    }

    @Override
    public String getName() {
        return "Couple";
    }

    @Override
    public String getDescription(Player player) {
        return LguhcPower.text(player, LangLguhc.ABILITY_COUPLE);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (used) {
            LguhcPower.send(player, LangLguhc.MSG_CUPIDON_ALREADY);
            Fx.Sounds.fail(player);
            return false;
        }
        if (args.length < 2) {
            LguhcPower.send(player, LangLguhc.POWER_USAGE, Map.of("%usage%", "/role couple <joueur1> <joueur2>"));
            Fx.Sounds.fail(player);
            return false;
        }
        Player first = LguhcPower.requireNamedPlaying(player, args[0]);
        if (first == null) return false;
        Player second = LguhcPower.requireNamedPlaying(player, args[1]);
        if (second == null) return false;
        if (first.equals(second)) {
            LguhcPower.send(player, LangLguhc.POWER_SELF);
            Fx.Sounds.fail(player);
            return false;
        }
        Bonds.link(first.getUniqueId(), second.getUniqueId())
                .dieTogether()
                .winTogether()
                .announce()
                .chat()
                .apply();
        used = true;
        LguhcPower.send(player, LangLguhc.MSG_CUPIDON_OK, Map.of("%a%", first.getName(), "%b%", second.getName()));
        Fx.Sounds.success(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return otherPlayingPlayerNames(player);
    }
}
