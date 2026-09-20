package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.Lang;

import java.util.Map;

public enum Lang3945 implements Lang {

    SCENARIO_DESC("§8UHC 39-45 §7est un UHC à rôles cachés. "
            + "Trois camps : §8§lAxe §7(organisé), §9§lRésistance §7(cellules fragmentées) et §f§lCivils §7(indépendants). "
            + "Les pouvoirs se débloquent par épisodes. Aucun rôle ne gagne un combat tout seul, ni ne révèle un camp entier."),

    POWER_LOCKED_EPISODE("§8§l39-45 §8│ §7Ce pouvoir se débloque à l'épisode §f%episode%§7."),
    POWER_NO_TARGET("§8§l39-45 §8│ §7Aucune cible dans votre ligne de vue."),
    POWER_PLAYER_NOT_FOUND("§8§l39-45 §8│ §7Joueur introuvable ou hors-jeu."),
    POWER_OUT_OF_RANGE("§8§l39-45 §8│ §7Cible trop éloignée (max. §f%range% §7blocs)."),
    POWER_USAGE("§8§l39-45 §8│ §7Usage : §f%usage%"),

    ROLE_DESC_COMMANDANT("\n  §8§lCOMMANDANT\n"
            + "  §7Camp §8Axe\n"
            + "  §8│ §7Officier unique. Le camp se connaît.\n"
            + "  §8│ §7§fOrdre §7(ép. 2) : Speed I + Résistance I aux Axe proches.\n"
            + "  §8│ §7Sa mort affaiblit la charge des Soldats.\n"),

    ROLE_DESC_OFFICIER("\n  §8§lOFFICIER\n"
            + "  §7Camp §8Axe\n"
            + "  §8│ §7Relais du Commandant. Coordination limitée.\n"
            + "  §8│ §7§f/role rapport §7(ép. 2) : position approximative du Commandant.\n"),

    ROLE_DESC_SOLDAT("\n  §8§lSOLDAT\n"
            + "  §7Camp §8Axe §7(rôle de masse)\n"
            + "  §8│ §7Combattant. Connaît ses camarades.\n"
            + "  §8│ §7§fCharge §7(ép. 2) : Speed II + Résistance I, cooldown long.\n"
            + "  §8│ §7Sans Commandant, la charge est plus courte.\n"),

    ROLE_DESC_CHEF_RESEAU("\n  §9§lCHEF DE RÉSEAU\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Organise une cellule. L'authentification arrivera plus tard.\n"
            + "  §8│ §7§fSignal §7(ép. 3) : nombre de présences et direction approximative, sans identités.\n"),

    ROLE_DESC_RESISTANT("\n  §9§lRÉSISTANT\n"
            + "  §7Camp §9Résistance §7(rôle de masse)\n"
            + "  §8│ §7Membre d'une cellule fragmentée.\n"
            + "  §8│ §7§fCachette §7(ép. 2) : invisibilité brève pour rompre un contact.\n"),

    ROLE_DESC_MEDECIN("\n  §9§lMÉDECIN\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Soutien limité, ressources rares.\n"
            + "  §8│ §7§fSoin §7(ép. 2) : restaure quelques cœurs à la cible visée. Utilisations limitées.\n"),

    ROLE_DESC_SABOTEUR("\n  §9§lSABOTEUR\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Neutralise un avantage adverse, sans tuer à sa place.\n"
            + "  §8│ §7§fSabotage §7(ép. 4) : silence temporaire des pouvoirs + perte des buffs de combat.\n"),

    ROLE_DESC_CIVIL("\n  §f§lCIVIL\n"
            + "  §7Camp §fCivils §7(indépendant)\n"
            + "  §8│ §7Survit pour lui-même. Victoire solo.\n"
            + "  §8│ §7§fInstinct §7(ép. 3, 1×/épisode) : présence proche, sans nom.\n"
            + "  §8│ §7§f/role allegeance §7(ép. 5, 1×) : rester indépendant ou soutenir ponctuellement.\n"),

    ROLE_DESC_INFORMATEUR("\n  §f§lINFORMATEUR\n"
            + "  §7Camp §fCivils\n"
            + "  §8│ §7Renseignements faibles, à transmettre à qui tu veux.\n"
            + "  §8│ §7§f/role analyser <joueur> §7(ép. 2) : santé / distance / équipement, jamais le rôle.\n"
            + "  §8│ §7§f/role transmettre <joueur> §7(ép. 3) : envoie ton dernier rapport.\n"),

    ROLE_DESC_CONTREBANDIER("\n  §f§lCONTREBANDIER\n"
            + "  §7Camp §fCivils\n"
            + "  §8│ §7Ressources rares et échanges.\n"
            + "  §8│ §7§fColis §7(ép. 3) : or + pomme d'or. Sneak + visée pour le donner.\n"),

    ABILITY_DESC_COMMANDANT_ORDRE("Clic : Speed I et Résistance I pour toi et les Axe dans le rayon. Épisode 2."),
    MSG_COMMANDANT_ORDRE("§8§l39-45 §8│ §7Ordre transmis. §f%count% §7Axe dans le rayon répondent."),
    MSG_COMMANDANT_DEATH("§8§l39-45 §8│ §cLe Commandant est tombé. Les charges des Soldats sont affaiblies."),
    MSG_COMMANDANT_DEATH_SELF("§8§l39-45 §8│ §7Vos officiers perdent leur coordination."),

    ABILITY_DESC_OFFICIER_RAPPORT("/role rapport : position approximative du Commandant. Jamais la liste du camp. Épisode 2."),
    MSG_OFFICIER_RAPPORT("§8§l39-45 §8│ §7Le Commandant est §f%distance%§7, direction §f%dir%§7."),
    MSG_OFFICIER_RAPPORT_DEAD("§8§l39-45 §8│ §7Le Commandant est hors combat."),
    MSG_OFFICIER_RAPPORT_WORLD("§8§l39-45 §8│ §7Le Commandant est dans une autre dimension."),
    MSG_OFFICIER_RAPPORT_UNKNOWN("§8§l39-45 §8│ §7Position du Commandant inconnue pour le moment."),

    ABILITY_DESC_SOLDAT_CHARGE("Clic : Speed II + Résistance I, courte durée, long cooldown. Sans Commandant : plus faible. Épisode 2."),
    MSG_SOLDAT_CHARGE("§8§l39-45 §8│ §7Charge lancée."),
    MSG_SOLDAT_CHARGE_WEAK("§8§l39-45 §8│ §7Charge affaiblie : le Commandant n'est plus là."),

    ABILITY_DESC_CHEF_SIGNAL("Clic : nombre de joueurs proches et direction de la présence la plus proche, sans noms. Épisode 3."),
    MSG_CHEF_SIGNAL_NONE("§8§l39-45 §8│ §7Aucune présence détectée dans le rayon."),
    MSG_CHEF_SIGNAL("§8§l39-45 §8│ §7Présences : §f%count%§7. La plus proche est §f%distance%§7 vers le §f%dir%§7."),

    ABILITY_DESC_RESISTANT_CACHETTE("Clic : invisibilité brève pour rompre un contact. Épisode 2."),
    MSG_RESISTANT_CACHETTE("§8§l39-45 §8│ §7Tu disparais un instant."),

    ABILITY_DESC_MEDECIN_SOIN("Clic en visant un joueur : soigne quelques cœurs. Utilisations limitées. Épisode 2."),
    MSG_MEDECIN_SOIN("§8§l39-45 §8│ §7Tu soignes §f%target%§7."),
    MSG_MEDECIN_SOIN_SELF("§8§l39-45 §8│ §7Tu te soignes."),
    MSG_MEDECIN_FULL("§8§l39-45 §8│ §7Cette personne est déjà en pleine forme."),

    ABILITY_DESC_SABOTEUR("Clic en visant un joueur : silence des pouvoirs et perte des buffs de combat, courte durée. Épisode 4."),
    MSG_SABOTEUR("§8§l39-45 §8│ §7Tu sabotes l'avantage de §f%target%§7."),
    MSG_SABOTEUR_TARGET("§8§l39-45 §8│ §cUn sabotage perturbe tes capacités."),

    ABILITY_DESC_CIVIL_INSTINCT("Clic : une fois par épisode, sait si quelqu'un est proche — sans nom. Épisode 3."),
    MSG_CIVIL_INSTINCT_YES("§8§l39-45 §8│ §7Tu sens une présence proche."),
    MSG_CIVIL_INSTINCT_NO("§8§l39-45 §8│ §7Rien d'anormal autour de toi."),
    MSG_CIVIL_INSTINCT_USED("§8§l39-45 §8│ §7Instinct déjà utilisé pendant cet épisode."),

    ABILITY_DESC_CIVIL_ALLEGEANCE("/role allegeance rester|soutenir : choix unique tardif, sans changer de camp. Épisode 5."),
    MSG_CIVIL_ALLEGEANCE_RESTER("§8§l39-45 §8│ §7Tu restes indépendant et récupères des réserves."),
    MSG_CIVIL_ALLEGEANCE_SOUTENIR("§8§l39-45 §8│ §7Tu aides ponctuellement à fuir. Tu n'as pas changé de camp."),

    ABILITY_DESC_INFORMATEUR_ANALYSE("/role analyser <joueur> : santé, distance approximative et équipement. Jamais le rôle ni le camp. Épisode 2."),
    MSG_INFORMATEUR_ANALYSE("§8§l39-45 §8│ §7Rapport : §f%target% §7est §f%health%§7, équipement §f%gear%§7, §f%distance%§7."),
    ABILITY_DESC_INFORMATEUR_TRANSMETTRE("/role transmettre <joueur> : envoie ton dernier rapport. Épisode 3."),
    MSG_INFORMATEUR_TRANSMETTRE("§8§l39-45 §8│ §7Rapport transmis à §f%target%§7."),
    MSG_INFORMATEUR_TRANSMETTRE_RECEIVE("§8§l39-45 §8│ §7Un informateur te glisse : §f%report%"),
    MSG_INFORMATEUR_NO_REPORT("§8§l39-45 §8│ §7Aucun rapport à transmettre. Analyse d'abord quelqu'un."),

    ABILITY_DESC_CONTREBANDIER("Clic : colis (or + pomme d'or). Sneak en visant un joueur pour le lui donner. Épisode 3."),
    MSG_CONTREBANDIER_SELF("§8§l39-45 §8│ §7Colis récupéré."),
    MSG_CONTREBANDIER_GIVE("§8§l39-45 §8│ §7Colis remis à §f%target%§7."),
    MSG_CONTREBANDIER_RECEIVE("§8§l39-45 §8│ §7Un contrebandier te glisse un colis."),
    ;

    private final Map<String, String> translations;

    Lang3945(String fr) {
        this.translations = Map.of("fr_FR", fr);
    }

    @Override
    public String getKey() {
        return "uhc3945." + name();
    }

    @Override
    public Map<String, String> getTranslations() {
        return translations;
    }
}
