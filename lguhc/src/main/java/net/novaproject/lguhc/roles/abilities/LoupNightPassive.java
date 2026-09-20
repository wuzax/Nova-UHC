package net.novaproject.lguhc.roles.abilities;

import net.novaproject.novauhc.ability.template.PassiveAbility;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.LguhcPower;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoupNightPassive extends PassiveAbility {

    public LoupNightPassive() {
        setCooldown(0);
    }

    @Override
    public String getName() {
        return "Nuit";
    }

    @Override
    public String getDescription(Player player) {
        return LguhcPower.text(player, LangLguhc.ABILITY_NIGHT);
    }

    @Override
    public boolean onEnable(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 80, 0, false, false), true);
        return true;
    }
}
