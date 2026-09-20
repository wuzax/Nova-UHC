package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.roles.abilities.CivilAllegeanceCommand;
import net.novaproject.uhc3945.roles.abilities.CivilInstinctActive;
import org.bukkit.Material;

public class CivilNeutre extends Role3945 {

    @Var(name = "Instinct", desc = "Présence proche, 1× par épisode. Épisode 3.", type = VariableType.ABILITY)
    public Ability instinct;

    @Var(name = "Allégeance", desc = "Choix unique tardif, sans changer de camp. Épisode 5.", type = VariableType.ABILITY)
    public Ability allegeance;

    public CivilNeutre() {
        setCamp(Camps3945.CIVILIAN);
        this.instinct = new CivilInstinctActive();
        this.allegeance = new CivilAllegeanceCommand();
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
