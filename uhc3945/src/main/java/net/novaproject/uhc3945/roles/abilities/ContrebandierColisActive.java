package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.template.UseAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.ability.toolbox.TargetSelector;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ContrebandierColisActive extends UseAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel le colis est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 3;

    @Var(name = "Portée d'échange", desc = "Distance pour remettre le colis en sneak.", type = VariableType.DOUBLE, min = 3, max = 10)
    private double range = 5;

    @Var(name = "Lingots d'or", desc = "Lingots d'or dans le colis.", type = VariableType.INTEGER, min = 1, max = 8)
    private int gold = 4;

    @Var(name = "Pommes d'or", desc = "Pommes d'or dans le colis.", type = VariableType.INTEGER, min = 1, max = 3)
    private int apples = 1;

    public ContrebandierColisActive() {
        setCooldown(180);
        setMaxUse(2);
    }

    @Override
    public String getName() {
        return "Colis";
    }

    @Override
    public Material getMaterial() {
        return Material.CHEST;
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_CONTREBANDIER);
    }

    @Override
    public boolean onEnable(Player player) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;

        Player target = player;
        if (player.isSneaking()) {
            Player looked = TargetSelector.lookedAt(player, range);
            if (looked != null) target = looked;
        }

        givePackage(target);
        if (target.equals(player)) {
            Power3945.send(player, Lang3945.MSG_CONTREBANDIER_SELF);
        } else {
            Power3945.send(player, Lang3945.MSG_CONTREBANDIER_GIVE, Map.of("%target%", target.getName()));
            Power3945.send(target, Lang3945.MSG_CONTREBANDIER_RECEIVE);
            Fx.Sounds.pulse(target);
        }
        Fx.Sounds.success(player);
        return true;
    }

    private void givePackage(Player target) {
        target.getInventory().addItem(
                new ItemStack(Material.GOLD_INGOT, gold),
                new ItemStack(Material.GOLDEN_APPLE, apples));
    }
}
