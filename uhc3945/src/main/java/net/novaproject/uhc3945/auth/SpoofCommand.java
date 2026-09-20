package net.novaproject.uhc3945.auth;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SpoofCommand extends CommandAbility {

    public SpoofCommand() {
        setCooldown(45);
        setMaxUse(3);
    }

    @Override
    public String getCommandKey() {
        return "usurper";
    }

    @Override
    public String getName() {
        return "Usurper un indice";
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(Lang3945.ABILITY_SPOOF_DESC, player);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return false;
        }
        String signal = args == null ? "" : String.join(" ", args);
        return scenario.auth().spoof(player, signal);
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }
}
