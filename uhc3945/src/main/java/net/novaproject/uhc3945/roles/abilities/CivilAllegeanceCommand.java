package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.template.CommandAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CivilAllegeanceCommand extends CommandAbility {

    @Var(name = "Épisode minimum", desc = "Épisode du choix d'allégeance.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 5;

    @Var(name = "Pommes (rester)", desc = "Pommes d'or si le Civil reste indépendant.", type = VariableType.INTEGER, min = 1, max = 3)
    private int apples = 2;

    @Var(name = "Durée soutien (s)", desc = "Durée de Speed I si le Civil soutient ponctuellement.", type = VariableType.TIME, min = 15, max = 90)
    private int supportDuration = 45;

    public CivilAllegeanceCommand() {
        setCooldown(0);
        setMaxUse(1);
    }

    @Override
    public String getCommandKey() {
        return "allegeance";
    }

    @Override
    public String getName() {
        return "Allégeance";
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_CIVIL_ALLEGEANCE);
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;
        if (args.length == 0) {
            Power3945.send(player, Lang3945.POWER_USAGE, Map.of("%usage%", "/role allegeance rester|soutenir"));
            Fx.Sounds.fail(player);
            return false;
        }

        String choice = args[0].toLowerCase(Locale.ROOT);
        if (choice.equals("rester") || choice.equals("seul") || choice.equals("independant")) {
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, apples));
            Power3945.send(player, Lang3945.MSG_CIVIL_ALLEGEANCE_RESTER);
            Fx.Sounds.success(player);
            return true;
        }
        if (choice.equals("soutenir") || choice.equals("aider") || choice.equals("resistance")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, supportDuration * 20, 0), true);
            Power3945.send(player, Lang3945.MSG_CIVIL_ALLEGEANCE_SOUTENIR);
            Fx.Sounds.success(player);
            return true;
        }

        Power3945.send(player, Lang3945.POWER_USAGE, Map.of("%usage%", "/role allegeance rester|soutenir"));
        Fx.Sounds.fail(player);
        return false;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        if (args != null && args.length <= 1) return List.of("rester", "soutenir");
        return List.of();
    }
}
