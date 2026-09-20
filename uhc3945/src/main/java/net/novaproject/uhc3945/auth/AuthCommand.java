package net.novaproject.uhc3945.auth;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class AuthCommand extends CommandAbility {

    public AuthCommand() {
        setCooldown(8);
        setMaxUse(-1);
    }

    @Override
    public String getCommandKey() {
        return "auth";
    }

    @Override
    public String getName() {
        return "Authentification";
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(Lang3945.ABILITY_AUTH_DESC, player);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return false;
        }
        String code = args == null ? "" : String.join(" ", args);
        return scenario.auth().submitCode(player, code);
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }
}
