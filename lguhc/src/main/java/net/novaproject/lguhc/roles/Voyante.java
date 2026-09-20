package net.novaproject.lguhc.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.lguhc.CampsLguhc;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.RoleLguhc;
import net.novaproject.lguhc.roles.abilities.VoyanteVoirCommand;
import org.bukkit.Material;

public class Voyante extends RoleLguhc {

    @Var(name = "Voir", desc = "/role voir <joueur> — révèle le rôle, 1×.", type = VariableType.ABILITY)
    public Ability voir;

    public Voyante() {
        setCamp(CampsLguhc.VILLAGE);
        setStrength(0);
        setResistance(0);
        this.voir = new VoyanteVoirCommand();
    }

    @Override
    public String getName() {
        return "Voyante";
    }

    @Override
    public Material getIconMaterial() {
        return Material.EYE_OF_ENDER;
    }

    @Override
    public Lang getDescriptionLang() {
        return LangLguhc.ROLE_DESC_VOYANTE;
    }
}
