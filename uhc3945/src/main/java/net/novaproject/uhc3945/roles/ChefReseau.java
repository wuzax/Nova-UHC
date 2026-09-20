package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import org.bukkit.Material;

public class ChefReseau extends Role3945 {

    public ChefReseau() {
        setCamp(Camps3945.RESISTANCE);
    }

    @Override
    public String getName() {
        return "Chef de réseau";
    }

    @Override
    public Material getIconMaterial() {
        return Material.COMPASS;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_CHEF_RESEAU;
    }
}
