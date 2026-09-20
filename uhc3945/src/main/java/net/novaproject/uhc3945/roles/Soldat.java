package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.roles.abilities.SoldatChargeActive;
import org.bukkit.Material;

public class Soldat extends Role3945 {

    @Var(name = "Charge", desc = "Speed II + Résistance I, courte durée. Épisode 2.", type = VariableType.ABILITY)
    public Ability charge;

    public Soldat() {
        setCamp(Camps3945.AXE);
        this.charge = new SoldatChargeActive();
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
