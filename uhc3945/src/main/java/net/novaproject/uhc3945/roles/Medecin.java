package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.roles.abilities.MedecinSoinActive;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Medecin extends Role3945 {

    @Var(name = "Soin", desc = "Soigne quelques cœurs. Utilisations limitées. Épisode 2.", type = VariableType.ABILITY)
    public Ability soin;

    public Medecin() {
        setCamp(Camps3945.RESISTANCE);
        this.soin = new MedecinSoinActive();
    }

    @Override
    public String getName() {
        return "Médecin";
    }

    @Override
    public Material getIconMaterial() {
        return Material.SPECKLED_MELON;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_MEDECIN;
    }

    @Override
    public void onGive(UHCPlayer uhcPlayer) {
        super.onGive(uhcPlayer);
        Player player = uhcPlayer.getPlayer();
        if (player == null) return;
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
    }
}
