package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import org.bukkit.Material;

public class Informateur extends Role3945 {

    public Informateur() {
        setCamp(Camps3945.CIVILIAN);
    }

    @Override
    public String getName() {
        return "Informateur";
    }

    @Override
    public Material getIconMaterial() {
        return Material.PAPER;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_INFORMATEUR;
    }
}
