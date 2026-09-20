package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.roles.abilities.OfficierRapportCommand;
import org.bukkit.Material;

public class Officier extends Role3945 {

    @Var(name = "Rapport", desc = "Position approximative du Commandant. Épisode 2.", type = VariableType.ABILITY)
    public Ability rapport;

    public Officier() {
        setCamp(Camps3945.AXE);
        this.rapport = new OfficierRapportCommand();
    }

    @Override
    public String getName() {
        return "Officier";
    }

    @Override
    public Material getIconMaterial() {
        return Material.IRON_HELMET;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_OFFICIER;
    }
}
