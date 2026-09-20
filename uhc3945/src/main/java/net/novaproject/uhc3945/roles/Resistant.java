package net.novaproject.uhc3945.roles;

import net.novaproject.novauhc.ability.Ability;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Role3945;
import net.novaproject.uhc3945.auth.AuthCommand;
import net.novaproject.uhc3945.auth.CodeCommand;
import net.novaproject.uhc3945.auth.ConfirmCommand;
import net.novaproject.uhc3945.roles.abilities.ResistantCachetteActive;
import org.bukkit.Material;

public class Resistant extends Role3945 {

    @Var(name = "Cachette", desc = "Invisibilité brève. Épisode 2.", type = VariableType.ABILITY)
    public Ability cachette;

    @Var(name = "Code", desc = "/role code — mot de reconnaissance de la cellule.", type = VariableType.ABILITY)
    private Ability code = new CodeCommand();

    @Var(name = "Authentification", desc = "/role auth <mot> [secret] — liaison entre cellules, jamais triviale.", type = VariableType.ABILITY)
    private Ability auth = new AuthCommand();

    @Var(name = "Confirmer", desc = "/role confirmer <joueur> — si le chef est hors-jeu.", type = VariableType.ABILITY)
    private Ability confirm = new ConfirmCommand();

    public Resistant() {
        setCamp(Camps3945.RESISTANCE);
        this.cachette = new ResistantCachetteActive();
    }

    @Override
    public String getName() {
        return "Résistant";
    }

    @Override
    public Material getIconMaterial() {
        return Material.BOW;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_RESISTANT;
    }
}
