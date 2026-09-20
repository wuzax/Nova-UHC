package net.novaproject.lguhc.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.lguhc.CampsLguhc;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.RoleLguhc;
import net.novaproject.lguhc.roles.abilities.PetiteFilleListenPassive;
import org.bukkit.Material;

public class PetiteFille extends RoleLguhc {

    @Var(name = "Écoute", desc = "Entend le chat des loups (préfixe lg).", type = VariableType.ABILITY)
    public Ability listen;

    public PetiteFille() {
        setCamp(CampsLguhc.VILLAGE);
        setStrength(0);
        setResistance(0);
        this.listen = new PetiteFilleListenPassive();
    }

    @Override
    public String getName() {
        return "Petite Fille";
    }

    @Override
    public Material getIconMaterial() {
        return Material.RED_ROSE;
    }

    @Override
    public Lang getDescriptionLang() {
        return LangLguhc.ROLE_DESC_PETITE_FILLE;
    }
}
