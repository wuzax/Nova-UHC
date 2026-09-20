package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.roles.abilities.SaboteurNeutraliseActive;
import org.bukkit.Material;

public class Saboteur extends Role3945 {

    @Var(name = "Sabotage", desc = "Silence temporaire des pouvoirs. Épisode 4.", type = VariableType.ABILITY)
    public Ability sabotage;

    public Saboteur() {
        setCamp(Camps3945.RESISTANCE);
        this.sabotage = new SaboteurNeutraliseActive();
    }

    @Override
    public String getName() {
        return "Saboteur";
    }

    @Override
    public Material getIconMaterial() {
        return Material.REDSTONE;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_SABOTEUR;
    }
}
