package net.novaproject.lguhc.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.scenario.role.reveal.KPIBuilder;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.lguhc.CampsLguhc;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.RoleLguhc;
import net.novaproject.lguhc.roles.abilities.LoupNightPassive;
import org.bukkit.Material;

public class LoupGarou extends RoleLguhc {

    @Var(name = "Nuit", desc = "Vision nocturne. Force de rôle le jour comme la nuit (UHC).", type = VariableType.ABILITY)
    public Ability night;

    public LoupGarou() {
        setCamp(CampsLguhc.LOUPS);
        setStrength(0.20);
        setResistance(0);
        this.night = new LoupNightPassive();
    }

    @Override
    public String getName() {
        return "Loup-Garou";
    }

    @Override
    public Material getIconMaterial() {
        return Material.ROTTEN_FLESH;
    }

    @Override
    public Lang getDescriptionLang() {
        return LangLguhc.ROLE_DESC_LOUP;
    }

    @Override
    public void registerKnowPlayers() {
        addKnowPlayer(KPIBuilder.ofCamp(CampsLguhc.LOUPS).showRole().build(this));
    }
}
