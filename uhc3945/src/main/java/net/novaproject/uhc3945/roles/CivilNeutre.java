package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import org.bukkit.Material;

public class CivilNeutre extends Role3945 {

    public CivilNeutre() {
        setCamp(Camps3945.CIVILIAN);
    }

    @Override
    public String getName() {
        return "Civil";
    }

    @Override
    public Material getIconMaterial() {
        return Material.WHEAT;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_CIVIL;
    }
}
