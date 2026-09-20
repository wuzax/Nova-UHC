package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.roles.abilities.ContrebandierColisActive;
import net.novaproject.uhc3945.win.PersonalObjective;
import net.novaproject.uhc3945.win.PersonalObjectives;
import org.bukkit.Material;

public class Contrebandier extends Role3945 {

    @Var(name = "Colis", desc = "Or + pomme d'or, ou don en sneak. Épisode 3.", type = VariableType.ABILITY)
    public Ability colis;

    public Contrebandier() {
        setCamp(Camps3945.CIVILIAN);
        this.colis = new ContrebandierColisActive();
    }

    @Override
    public PersonalObjective personalObjective() {
        return PersonalObjectives.SURVIVE;
    }

    @Override
    public String getName() {
        return "Contrebandier";
    }

    @Override
    public Material getIconMaterial() {
        return Material.CHEST;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_CONTREBANDIER;
    }
}
