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
import net.novaproject.uhc3945.roles.abilities.ChefSignalActive;
import org.bukkit.Material;

public class ChefReseau extends Role3945 {

    @Var(name = "Signal", desc = "Présences proches sans identités. Épisode 3.", type = VariableType.ABILITY)
    public Ability signal;

    @Var(name = "Code", desc = "/role code — mot de reconnaissance et mot secret de la cellule.", type = VariableType.ABILITY)
    private Ability code = new CodeCommand();

    @Var(name = "Authentification", desc = "/role auth <mot> [secret] — liaison entre cellules, jamais triviale.", type = VariableType.ABILITY)
    private Ability auth = new AuthCommand();

    @Var(name = "Confirmer", desc = "/role confirmer <joueur> — valide une liaison vers ta cellule.", type = VariableType.ABILITY)
    private Ability confirm = new ConfirmCommand();

    public ChefReseau() {
        setCamp(Camps3945.RESISTANCE);
        this.signal = new ChefSignalActive();
    }

    @Override
    public String getName() {
        return "Chef de réseau";
    }

    @Override
    public Material getIconMaterial() {
        return Material.COMPASS;
    }

    @Override
    public Lang getDescriptionLang() {
        return Lang3945.ROLE_DESC_CHEF_RESEAU;
    }
}
