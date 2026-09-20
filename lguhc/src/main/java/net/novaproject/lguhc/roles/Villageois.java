package net.novaproject.lguhc.roles;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.lguhc.CampsLguhc;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.RoleLguhc;
import org.bukkit.Material;

public class Villageois extends RoleLguhc {

    public Villageois() {
        setCamp(CampsLguhc.VILLAGE);
        setStrength(0);
        setResistance(0);
    }

    @Override
    public String getName() {
        return "Villageois";
    }

    @Override
    public Material getIconMaterial() {
        return Material.WHEAT;
    }

    @Override
    public Lang getDescriptionLang() {
        return LangLguhc.ROLE_DESC_VILLAGEOIS;
    }
}
