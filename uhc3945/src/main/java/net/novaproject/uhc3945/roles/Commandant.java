package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import org.bukkit.Material;

public class Commandant extends Role3945 {

    public Commandant() {
        setCamp(Camps3945.AXE);
    }

    @Override
    public String getName() {
        return "Commandant";
    }

    @Override
    public Material getIconMaterial() {
        return Material.GOLD_SWORD;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_COMMANDANT;
    }
}
