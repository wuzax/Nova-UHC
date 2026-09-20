package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.roles.abilities.CommandantOrdreActive;
import org.bukkit.Material;

public class Commandant extends Role3945 {

    @Var(name = "Ordre", desc = "Speed I + Résistance I aux Axe proches. Épisode 2.", type = VariableType.ABILITY)
    public Ability ordre;

    public Commandant() {
        setCamp(Camps3945.AXE);
        this.ordre = new CommandantOrdreActive();
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
