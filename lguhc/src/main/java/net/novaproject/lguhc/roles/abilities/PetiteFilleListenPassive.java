package net.novaproject.lguhc.roles.abilities;

import net.novaproject.novauhc.ability.template.PassiveAbility;
import net.novaproject.novauhc.utils.chat.ChatManager;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.LguhcPower;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PetiteFilleListenPassive extends PassiveAbility {

    public PetiteFilleListenPassive() {
        setCooldown(0);
    }

    @Override
    public String getName() {
        return "Écoute";
    }

    @Override
    public String getDescription(Player player) {
        return LguhcPower.text(player, LangLguhc.ABILITY_LISTEN);
    }

    @Override
    public boolean onEnable(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 80, 0, false, false), true);
        ChatManager.get().addIndirectReader(LguhcPower.WOLF_CHAT_ID, player);
        return true;
    }
}
