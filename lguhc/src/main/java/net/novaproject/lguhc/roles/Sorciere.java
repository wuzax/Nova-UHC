package net.novaproject.lguhc.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.lguhc.CampsLguhc;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.RoleLguhc;
import net.novaproject.lguhc.roles.abilities.SorciereHealCommand;
import net.novaproject.lguhc.roles.abilities.SorciereKillCommand;
import org.bukkit.Material;

public class Sorciere extends RoleLguhc {

    @Var(name = "Soigner", desc = "/role soigner <joueur> — 1×.", type = VariableType.ABILITY)
    public Ability heal;

    @Var(name = "Tuer", desc = "/role tuer <joueur> — 1×.", type = VariableType.ABILITY)
    public Ability kill;

    public Sorciere() {
        setCamp(CampsLguhc.VILLAGE);
        setStrength(0);
        setResistance(0);
        this.heal = new SorciereHealCommand();
        this.kill = new SorciereKillCommand();
    }

    @Override
    public String getName() {
        return "Sorcière";
    }

    @Override
    public Material getIconMaterial() {
        return Material.POTION;
    }

    @Override
    public Lang getDescriptionLang() {
        return LangLguhc.ROLE_DESC_SORCIERE;
    }
}
