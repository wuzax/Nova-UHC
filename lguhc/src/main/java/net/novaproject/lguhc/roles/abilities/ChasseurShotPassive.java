package net.novaproject.lguhc.roles.abilities;

import net.novaproject.novauhc.ability.template.PassiveAbility;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.LguhcPower;
import org.bukkit.entity.Player;

public class ChasseurShotPassive extends PassiveAbility {

    public ChasseurShotPassive() {
        setCooldown(0);
    }

    @Override
    public String getName() {
        return "Tir";
    }

    @Override
    public String getDescription(Player player) {
        return LguhcPower.text(player, LangLguhc.ABILITY_SHOT);
    }

    @Override
    public boolean onEnable(Player player) {
        return false;
    }
}
