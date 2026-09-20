package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.scenario.role.CampWinPolicy;
import net.novaproject.novauhc.scenario.role.ModeKit;
import net.novaproject.novauhc.scenario.role.ScenarioRole;
import net.novaproject.novauhc.scenario.role.camps.Camps;
import net.novaproject.novauhc.utils.item.ItemCreator;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.groups.GroupLimitService;
import net.novaproject.uhc3945.groups.GroupLimitSettings;
import net.novaproject.uhc3945.roles.ChefReseau;
import net.novaproject.uhc3945.roles.CivilNeutre;
import net.novaproject.uhc3945.roles.Commandant;
import net.novaproject.uhc3945.roles.Informateur;
import net.novaproject.uhc3945.roles.Resistant;
import net.novaproject.uhc3945.roles.Soldat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Scenario3945 extends ScenarioRole<Role3945> {

    private static Scenario3945 instance;

    @Var(category = "groupes", name = "Limites de groupe", desc = "Active la détection de proximité avant meetup.", type = VariableType.BOOLEAN)
    private boolean groupLimitsEnabled = true;

    @Var(category = "groupes", name = "Rayon (blocs)", desc = "Distance maximale pour compter un regroupement.", type = VariableType.INTEGER, min = 1, max = 128)
    private int groupRadius = 35;

    @Var(category = "groupes", name = "Tolérance", desc = "Secondes de présence avant avertissement.", type = VariableType.TIME, min = 0, max = 300)
    private int groupGraceSeconds = 25;

    @Var(category = "groupes", name = "Compte à rebours", desc = "Secondes après l'avertissement avant le malus.", type = VariableType.TIME, min = 1, max = 120)
    private int groupCountdownSeconds = 10;

    @Var(category = "groupes", name = "Max Axe", desc = "Joueurs de l'Axe maximum dans le rayon, avant meetup.", type = VariableType.INTEGER, min = 1, max = 36)
    private int groupAxisMax = 4;

    @Var(category = "groupes", name = "Max Résistance", desc = "Joueurs de la Résistance maximum dans le rayon, avant meetup.", type = VariableType.INTEGER, min = 1, max = 36)
    private int groupResistanceMax = 3;

    @Var(category = "groupes", name = "Max Civils", desc = "Civils maximum dans le rayon, avant meetup.", type = VariableType.INTEGER, min = 1, max = 36)
    private int groupCivilianMax = 3;

    @Var(category = "groupes", name = "Max avant rôles", desc = "Limite globale tant que les rôles ne sont pas distribués.", type = VariableType.INTEGER, min = 1, max = 36)
    private int groupPreRoleMax = 3;

    @Var(category = "groupes", name = "Désactiver au meetup", desc = "Lève les limites au meetup. Sinon, les plafonds meetup s'appliquent.", type = VariableType.BOOLEAN)
    private boolean groupDisableAtMeetup = true;

    @Var(category = "groupes", name = "Meetup max Axe", desc = "Plafond Axe si les limites restent actives au meetup.", type = VariableType.INTEGER, min = 1, max = 99)
    private int groupMeetupAxisMax = 12;

    @Var(category = "groupes", name = "Meetup max Résistance", desc = "Plafond Résistance si les limites restent actives au meetup.", type = VariableType.INTEGER, min = 1, max = 99)
    private int groupMeetupResistanceMax = 9;

    @Var(category = "groupes", name = "Meetup max Civils", desc = "Plafond Civils si les limites restent actives au meetup.", type = VariableType.INTEGER, min = 1, max = 99)
    private int groupMeetupCivilianMax = 9;

    @Var(category = "groupes", name = "Exception combat", desc = "Suspend le décompte pendant un combat.", type = VariableType.BOOLEAN)
    private boolean groupIgnoreCombat = true;

    @Var(category = "groupes", name = "Exception fuite", desc = "Suspend le décompte si le groupe s'éloigne.", type = VariableType.BOOLEAN)
    private boolean groupIgnoreFleeing = true;

    @Var(category = "groupes", name = "Révéler la zone", desc = "Annonce une position approximative en cas de malus.", type = VariableType.BOOLEAN)
    private boolean groupRevealZone = true;

    @Var(category = "groupes", name = "Précision de zone", desc = "Arrondi des coordonnées révélées, en blocs.", type = VariableType.INTEGER, min = 8, max = 200)
    private int groupRevealPrecision = 50;

    @Var(category = "groupes", name = "Couper les pouvoirs", desc = "Désactive temporairement les avantages de rôle pendant le malus.", type = VariableType.BOOLEAN)
    private boolean groupSuppressAbilities = true;

    @Var(category = "groupes", name = "Faiblesse", desc = "Niveau de Weakness du malus (0 = désactivé).", type = VariableType.INTEGER, min = 0, max = 3)
    private int groupWeaknessLevel = 1;

    @Var(category = "groupes", name = "Fatigue", desc = "Niveau de Mining Fatigue du malus (0 = désactivé).", type = VariableType.INTEGER, min = 0, max = 3)
    private int groupFatigueLevel = 1;

    @Var(category = "groupes", name = "Lenteur", desc = "Niveau de Slowness du malus (0 = désactivé).", type = VariableType.INTEGER, min = 0, max = 3)
    private int groupSlownessLevel = 0;

    public static Scenario3945 get() {
        return instance;
    }

    public GroupLimitSettings groupLimitSettings() {
        return new GroupLimitSettings(
                groupLimitsEnabled,
                groupRadius,
                groupGraceSeconds,
                groupCountdownSeconds,
                groupAxisMax,
                groupResistanceMax,
                groupCivilianMax,
                groupPreRoleMax,
                groupDisableAtMeetup,
                groupMeetupAxisMax,
                groupMeetupResistanceMax,
                groupMeetupCivilianMax,
                groupIgnoreCombat,
                groupIgnoreFleeing,
                groupRevealZone,
                groupRevealPrecision,
                groupSuppressAbilities,
                groupWeaknessLevel,
                groupFatigueLevel,
                groupSlownessLevel
        );
    }

    @Override
    public Camps[] getCamps() {
        return Camps3945.values();
    }

    @Override
    public String getName() {
        return "UHC 39-45";
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(Lang3945.SCENARIO_DESC, player);
    }

    @Override
    public ItemCreator getItem() {
        return new ItemCreator(Material.MAP).setName("§8§lUHC 39-45");
    }

    @Override
    public String getColor() {
        return "§8";
    }

    @Override
    public String getPrefix() {
        return "§8§l39-45 §8│";
    }

    @Override
    public void setup() {
        super.setup();
        instance = this;
        GroupLimitService.get().registerListener();
        ModeKit.of(this)
                .camp(Camps3945.AXE, CampWinPolicy::together)
                .camp(Camps3945.RESISTANCE, CampWinPolicy::together)
                .camp(Camps3945.CIVILIAN, CampWinPolicy::solo)
                .uniqueRoles(Commandant.class, ChefReseau.class, Informateur.class)
                .role(Soldat.class)
                .role(Resistant.class)
                .filler(CivilNeutre.class)
                .revealWithin(Camps3945.AXE)
                .apply();
        Bukkit.getLogger().info("[UHC 39-45] Scénario initialisé (camps, rôles stub, limites de groupe).");
    }

    @Override
    public void onGameStart() {
        GroupLimitService.get().reset();
    }

    @Override
    public void onStop() {
        super.onStop();
        GroupLimitService.get().reset();
    }
}
