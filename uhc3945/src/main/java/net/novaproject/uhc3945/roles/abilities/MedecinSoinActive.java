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

import java.util.Map;

public class MedecinSoinActive extends UseAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel le soin est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 2;

    @Var(name = "Portée", desc = "Distance maximale de la cible visée.", type = VariableType.DOUBLE, min = 3, max = 12)
    private double range = 6;

    @Var(name = "Cœurs rendus", desc = "Cœurs restaurés (pas de régénération continue).", type = VariableType.DOUBLE, min = 1, max = 4)
    private double hearts = 2;

    public MedecinSoinActive() {
        setCooldown(90);
        setMaxUse(3);
    }

    @Override
    public String getName() {
        return "Soin";
    }

    @Override
    public Material getMaterial() {
        return Material.SPECKLED_MELON;
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_MEDECIN_SOIN);
    }

    @Override
    public boolean onEnable(Player player) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;

        Player target = TargetSelector.lookedAt(player, range);
        if (target == null) target = player;
        if (target.getHealth() >= target.getMaxHealth() - 0.1) {
            Power3945.send(player, Lang3945.MSG_MEDECIN_FULL);
            Fx.Sounds.fail(player);
            return false;
        }
        heal(target);
        if (target.equals(player)) {
            Power3945.send(player, Lang3945.MSG_MEDECIN_SOIN_SELF);
        } else {
            Power3945.send(player, Lang3945.MSG_MEDECIN_SOIN, Map.of("%target%", target.getName()));
            Fx.Sounds.pulse(target);
        }
        Fx.Sounds.success(player);
        return true;
    }

    private void heal(Player target) {
        double amount = hearts * 2;
        target.setHealth(Math.min(target.getMaxHealth(), target.getHealth() + amount));
    }
}
