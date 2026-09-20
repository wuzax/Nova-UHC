package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.roles.abilities.InformateurAnalyseCommand;
import net.novaproject.uhc3945.roles.abilities.InformateurTransmettreCommand;
import net.novaproject.uhc3945.win.PersonalObjective;
import net.novaproject.uhc3945.win.PersonalObjectives;
import org.bukkit.Material;

public class Informateur extends Role3945 {

    @Var(name = "Analyser", desc = "Santé, distance et équipement. Jamais le rôle. Épisode 2.", type = VariableType.ABILITY)
    public Ability analyser;

    @Var(name = "Transmettre", desc = "Envoie le dernier rapport. Épisode 3.", type = VariableType.ABILITY)
    public Ability transmettre;

    public Informateur() {
        setCamp(Camps3945.CIVILIAN);
        this.analyser = new InformateurAnalyseCommand();
        this.transmettre = new InformateurTransmettreCommand();
    }

    @Override
    public PersonalObjective personalObjective() {
        return PersonalObjectives.INFORM;
    }

    @Override
    public String getName() {
        return "Informateur";
    }

    @Override
    public Material getIconMaterial() {
        return Material.PAPER;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_INFORMATEUR;
    }
}
