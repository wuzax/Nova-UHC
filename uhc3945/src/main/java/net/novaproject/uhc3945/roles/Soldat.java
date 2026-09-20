package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import org.bukkit.Material;

public class Soldat extends Role3945 {

    public Soldat() {
        setCamp(Camps3945.AXE);
    }

    @Override
    public String getName() {
        return "Soldat";
    }

    @Override
    public Material getIconMaterial() {
        return Material.IRON_CHESTPLATE;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_SOLDAT;
    }
}
