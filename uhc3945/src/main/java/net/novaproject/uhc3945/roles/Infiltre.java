package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.auth.InterceptCommand;
import net.novaproject.uhc3945.auth.SpoofCommand;
import org.bukkit.Material;

public class Infiltre extends Role3945 {

    @Var(name = "Intercepter", desc = "/role intercepter — capte un fragment d'authentification, jamais un roster.", type = VariableType.ABILITY)
    private Ability intercept = new InterceptCommand();

    @Var(name = "Usurper", desc = "/role usurper <mot> — rejoue un fragment volé (doute, pas d'auth complète).", type = VariableType.ABILITY)
    private Ability spoof = new SpoofCommand();

    public Infiltre() {
        setCamp(Camps3945.AXE);
    }

    @Override
    public String getName() {
        return "Infiltré";
    }

    @Override
    public Material getIconMaterial() {
        return Material.SPIDER_EYE;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_INFILTRE;
    }
}
