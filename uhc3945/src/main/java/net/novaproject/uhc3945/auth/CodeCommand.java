package net.novaproject.uhc3945.auth;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import org.bukkit.entity.Player;

public class CodeCommand extends CommandAbility {

    public CodeCommand() {
        setCooldown(0);
        setMaxUse(-1);
    }

    @Override
    public String getCommandKey() {
        return "code";
    }

    @Override
    public String getName() {
        return "Code";
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(Lang3945.ABILITY_CODE_DESC, player);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return false;
        }
        scenario.auth().showCode(player);
        return true;
    }
}
