package net.novaproject.uhc3945.auth;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import org.bukkit.entity.Player;

import java.util.Map;

public class InterceptCommand extends CommandAbility {

    @Var(name = "Durée d'écoute", desc = "Secondes pendant lesquelles l'Infiltré capte les fragments d'auth.", type = VariableType.TIME, min = 10, max = 300)
    private int listenSeconds = 90;

    public InterceptCommand() {
        setCooldown(120);
        setMaxUse(-1);
    }

    @Override
    public String getCommandKey() {
        return "intercepter";
    }

    @Override
    public String getName() {
        return "Intercepter";
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(Lang3945.ABILITY_INTERCEPT_DESC, player);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null) {
            return false;
        }
        scenario.auth().startListening(player.getUniqueId(), listenSeconds);
        LangManager.get().send(Lang3945.INFIL_INTERCEPT_START, player, Map.of("%seconds%", listenSeconds));
        return true;
    }
}
