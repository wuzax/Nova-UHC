package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.template.UseAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ResistantCachetteActive extends UseAbility {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel la cachette est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 2;

    @Var(name = "Durée (s)", desc = "Durée d'invisibilité.", type = VariableType.TIME, min = 2, max = 8)
    private int duration = 5;

    public ResistantCachetteActive() {
        setCooldown(300);
        setMaxUse(-1);
    }

    @Override
    public String getName() {
        return "Cachette";
    }

    @Override
    public Material getMaterial() {
        return Material.INK_SACK;
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_RESISTANT_CACHETTE);
    }

    @Override
    public boolean onEnable(Player player) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration * 20, 0), true);
        Power3945.send(player, Lang3945.MSG_RESISTANT_CACHETTE);
        Fx.Sounds.success(player);
        return true;
    }
}
