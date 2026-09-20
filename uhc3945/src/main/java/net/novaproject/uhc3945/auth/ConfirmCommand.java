package net.novaproject.uhc3945.auth;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfirmCommand extends CommandAbility {

    public ConfirmCommand() {
        setCooldown(5);
        setMaxUse(-1);
    }

    @Override
    public String getCommandKey() {
        return "confirmer";
    }

    @Override
    public String getName() {
        return "Confirmer une liaison";
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(Lang3945.ABILITY_CONFIRM_DESC, player);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return false;
        }
        String target = args != null && args.length > 0 ? args[0] : "";
        return scenario.auth().confirm(player, target);
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return super.onTabComplete(player, args);
    }
}
