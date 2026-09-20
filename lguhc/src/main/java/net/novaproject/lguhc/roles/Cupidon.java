package net.novaproject.lguhc.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.lguhc.CampsLguhc;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.RoleLguhc;
import net.novaproject.lguhc.roles.abilities.CupidonCoupleCommand;
import org.bukkit.Material;

public class Cupidon extends RoleLguhc {

    @Var(name = "Couple", desc = "/role couple <j1> <j2> — 1×.", type = VariableType.ABILITY)
    public Ability couple;

    public Cupidon() {
        setCamp(CampsLguhc.VILLAGE);
        setStrength(0);
        setResistance(0);
        this.couple = new CupidonCoupleCommand();
    }

    @Override
    public String getName() {
        return "Cupidon";
    }

    @Override
    public Material getIconMaterial() {
        return Material.ARROW;
    }

    @Override
    public Lang getDescriptionLang() {
        return LangLguhc.ROLE_DESC_CUPIDON;
    }
}
