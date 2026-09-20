package net.novaproject.uhc3945.auth;

import net.novaproject.novauhc.command.Command;
import net.novaproject.novauhc.command.CommandArguments;
import net.novaproject.novauhc.command.CommandManager;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import org.bukkit.entity.Player;

import java.util.List;

public final class NetworkCommands {

    private NetworkCommands() {
    }

    public static void register() {
        CommandManager.get().register("contacts", new ContactsCommand());
        CommandManager.get().register("camp", new CampCommand());
    }

    private static Scenario3945 activeScenario() {
        Scenario3945 scenario = Scenario3945.get();
        return scenario != null && scenario.isActive() ? scenario : null;
    }

    private static final class ContactsCommand extends Command.PlayerCommand {
        @Override
        protected void run(Player player, CommandArguments args) {
            Scenario3945 scenario = activeScenario();
            if (scenario == null) {
                LangManager.get().send(Lang3945.CMD_SCENARIO_INACTIVE, player);
                return;
            }
            UHCPlayer up = UHCPlayerManager.get().getPlayer(player);
            Role role = scenario.getRoleByUHCPlayer(up);
            if (role == null) {
                LangManager.get().send(Lang3945.CMD_NO_ROLE, player);
                return;
            }
            scenario.knowledge().sendContacts(player, role, scenario.cells());
        }

        @Override
        public List<String> tabComplete(CommandArguments args) {
            return List.of();
        }
    }

    private static final class CampCommand extends Command.PlayerCommand {
        @Override
        protected void run(Player player, CommandArguments args) {
            Scenario3945 scenario = activeScenario();
            if (scenario == null) {
                LangManager.get().send(Lang3945.CMD_SCENARIO_INACTIVE, player);
                return;
            }
            UHCPlayer up = UHCPlayerManager.get().getPlayer(player);
            Role role = scenario.getRoleByUHCPlayer(up);
            if (role == null) {
                LangManager.get().send(Lang3945.CMD_NO_ROLE, player);
                return;
            }
            scenario.knowledge().sendCamp(player, role, scenario.cells());
        }

        @Override
        public List<String> tabComplete(CommandArguments args) {
            return List.of();
        }
    }
}
