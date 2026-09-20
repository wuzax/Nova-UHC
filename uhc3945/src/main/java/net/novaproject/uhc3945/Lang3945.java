package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.Lang;

import java.util.Map;

public enum Lang3945 implements Lang {

    SCENARIO_DESC("§8UHC 39-45 §7est un UHC à rôles cachés sur le thème de la Seconde Guerre mondiale. "
            + "Trois camps : §8§lAxe §7(noyau organisé + Infiltré isolé), "
            + "§9§lRésistance §7(cellules 2-3, authentification) et §f§lCivils §7(indépendants). "
            + "L'Axe gagne en neutralisant la Résistance, la Résistance en neutralisant l'Axe. "
            + "Les civils ont un objectif personnel et ne bloquent pas la victoire militaire. "
            + "Les pouvoirs se débloquent par épisodes. Avant le meetup, les regroupements sont limités : "
            + "§8Axe 4§7, §9Résistance 3§7, §fCivils 3§7. "
            + "Commandes : §f/role§7, §f/contacts§7, §f/camp§7, §f/role code§7, §f/role auth§7."),

    GROUP_WARN("§8§l39-45 §8│ §cRegroupement trop important — %camp% §8(§e%count%§8/§e%max%§8)§c. Séparez-vous."),
    GROUP_COUNTDOWN("§cGroupe trop important §8— §f%seconds%s §7pour vous disperser"),
    GROUP_DEBUFF("§8§l39-45 §8│ §cMalus de regroupement appliqué. Éloignez-vous (§e%count%§8/§e%max%§c)."),
    GROUP_DEBUFF_BAR("§cMalus de groupe §8— §7disperser le camp %camp% §8(§e%count%§8/§e%max%§8)"),
    GROUP_PAUSE("§eLimite de groupe en pause §8— §7%reason%"),
    GROUP_PAUSE_COMBAT("combat en cours"),
    GROUP_PAUSE_FLEE("dispersion détectée"),
    GROUP_ZONE("§8§l39-45 §8│ §eRegroupement suspect du camp %camp% §evers §fX:%x% Z:%z% §7(approximatif)."),
    GROUP_MEETUP_OFF("§8§l39-45 §8│ §aLes limites de groupe sont levées (meetup)."),
    GROUP_MEETUP_RAISE("§8§l39-45 §8│ §eLimites de groupe assouplies (meetup) : "
            + "§8Axe %axe% §8│ §9Résistance %resistance% §8│ §fCivils %civilians%§e."),

    POWER_LOCKED_EPISODE("§8§l39-45 §8│ §7Ce pouvoir se débloque à l'épisode §f%episode%§7."),
    POWER_NO_TARGET("§8§l39-45 §8│ §7Aucune cible dans votre ligne de vue."),
    POWER_PLAYER_NOT_FOUND("§8§l39-45 §8│ §7Joueur introuvable ou hors-jeu."),
    POWER_OUT_OF_RANGE("§8§l39-45 §8│ §7Cible trop éloignée (max. §f%range% §7blocs)."),
    POWER_USAGE("§8§l39-45 §8│ §7Usage : §f%usage%"),

    ROLE_DESC_COMMANDANT("\n  §8§lCOMMANDANT\n"
            + "  §7Camp §8Axe §7(noyau)\n"
            + "  §8│ §7Connaît Soldats et Officier, pas l'Infiltré.\n"
            + "  §8│ §7Victoire : §8neutraliser la Résistance §7et survivre avec l'Axe.\n"
            + "  §8│ §7§fOrdre §7(ép. 2) : Speed I + Résistance I aux Axe du noyau proches.\n"
            + "  §8│ §7Sa mort affaiblit la charge des Soldats.\n"),

    ROLE_DESC_OFFICIER("\n  §8§lOFFICIER\n"
            + "  §7Camp §8Axe §7(noyau)\n"
            + "  §8│ §7Relais du Commandant. Coordination limitée.\n"
            + "  §8│ §7Victoire : §8neutraliser la Résistance §7et survivre avec l'Axe.\n"
            + "  §8│ §7§f/role rapport §7(ép. 2) : position approximative du Commandant.\n"),

    ROLE_DESC_SOLDAT("\n  §8§lSOLDAT\n"
            + "  §7Camp §8Axe §7(noyau, masse)\n"
            + "  §8│ §7Connaît le Commandant, l'Officier et les autres Soldats. Pas l'Infiltré.\n"
            + "  §8│ §7Victoire : §8neutraliser la Résistance §7et survivre avec l'Axe.\n"
            + "  §8│ §7§fCharge §7(ép. 2) : Speed II + Résistance I, cooldown long.\n"
            + "  §8│ §7Sans Commandant, la charge est plus courte.\n"),

    ROLE_DESC_INFILTRE("\n  §8§lINFILTRÉ\n"
            + "  §7Camp §8Axe §7(isolé du noyau)\n"
            + "  §8│ §7N'apparaît pas sur la liste des alliés de l'Axe.\n"
            + "  §8│ §7Victoire : §8neutraliser la Résistance §7avec l'Axe.\n"
            + "  §8│ §7§f/role intercepter §7puis §f/role usurper <mot> §7: fragment d'auth, jamais le roster.\n"),

    ROLE_DESC_CHEF_RESEAU("\n  §9§lCHEF DE RÉSEAU\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Connaît sa cellule. Détient le mot secret. Valide les liaisons.\n"
            + "  §8│ §7Victoire : §9neutraliser l'Axe §7et survivre avec la Résistance.\n"
            + "  §8│ §7§fSignal §7(ép. 3) : présences + direction, sans identités.\n"
            + "  §8│ §7§f/role code§7, §f/role auth <mot> [secret]§7, §f/role confirmer <joueur>\n"),

    ROLE_DESC_RESISTANT("\n  §9§lRÉSISTANT\n"
            + "  §7Camp §9Résistance §7(masse)\n"
            + "  §8│ §7Connaît uniquement sa cellule. Les autres réseaux restent à authentifier.\n"
            + "  §8│ §7Victoire : §9neutraliser l'Axe §7et survivre avec la Résistance.\n"
            + "  §8│ §7§fCachette §7(ép. 2) : invisibilité brève.\n"
            + "  §8│ §7§f/role code§7, §f/role auth <mot> [secret]§7, §f/role confirmer <joueur>\n"),

    ROLE_DESC_MEDECIN("\n  §9§lMÉDECIN\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Soutien limité. Connaît sa cellule, pas tout le camp.\n"
            + "  §8│ §7Victoire : §9neutraliser l'Axe §7et survivre avec la Résistance.\n"
            + "  §8│ §7§fSoin §7(ép. 2) : quelques cœurs, utilisations limitées.\n"
            + "  §8│ §7§f/role code§7, §f/role auth <mot> [secret]§7, §f/role confirmer <joueur>\n"),

    ROLE_DESC_SABOTEUR("\n  §9§lSABOTEUR\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Neutralise un avantage adverse, sans tuer à sa place.\n"
            + "  §8│ §7Victoire : §9neutraliser l'Axe §7et survivre avec la Résistance.\n"
            + "  §8│ §7§fSabotage §7(ép. 4) : silence des pouvoirs + perte des buffs.\n"
            + "  §8│ §7§f/role code§7, §f/role auth <mot> [secret]§7, §f/role confirmer <joueur>\n"),

    ROLE_DESC_CIVIL("\n  §f§lCIVIL\n"
            + "  §7Camp §fCivils §7(indépendant)\n"
            + "  §8│ §7Objectif personnel : §fsurvivre.\n"
            + "  §8│ §7Tu gagnes si tu es encore en vie à la victoire militaire, ou s'il ne reste que des civils.\n"
            + "  §8│ §7§fInstinct §7(ép. 3, 1×/épisode) : présence proche, sans nom.\n"
            + "  §8│ §7§f/role allegeance §7(ép. 5, 1×) : rester indépendant ou soutenir ponctuellement.\n"),

    ROLE_DESC_INFORMATEUR("\n  §f§lINFORMATEUR\n"
            + "  §7Camp §fCivils\n"
            + "  §8│ §7Objectif personnel : §fsurvivre §7ou §ftransmettre un renseignement§7.\n"
            + "  §8│ §7Tu gagnes personnellement, indépendamment du camp militaire vainqueur.\n"
            + "  §8│ §7§f/role analyser <joueur> §7(ép. 2) : santé / distance / équipement, jamais le rôle.\n"
            + "  §8│ §7§f/role transmettre <joueur> §7(ép. 3) : envoie ton dernier rapport.\n"),

    ROLE_DESC_CONTREBANDIER("\n  §f§lCONTREBANDIER\n"
            + "  §7Camp §fCivils\n"
            + "  §8│ §7Objectif personnel : §fsurvivre.\n"
            + "  §8│ §7Tu gagnes si tu es encore en vie à la victoire militaire, ou s'il ne reste que des civils.\n"
            + "  §8│ §7§fColis §7(ép. 3) : or + pomme d'or. Sneak + visée pour le donner.\n"),

    ROLE_PERSONAL_OBJECTIVE("  §8│ §7Objectif personnel : §f%objective%"),
    ROLE_CARD_CELL("  §8│ §7Ta cellule : §9%cell%"),
    ROLE_CARD_ISOLATE("  §8│ §7Tu es un agent isolé, sans cellule."),

    OBJ_SURVIVE("survivre jusqu'à la fin"),
    OBJ_EXFILTRATE("s'exfiltrer (hook)"),
    OBJ_INFORM("survivre / transmettre un renseignement"),

    WIN_LABEL_AXE("§8§lAxe"),
    WIN_LABEL_RESISTANCE("§9§lRésistance"),
    WIN_LABEL_CIVILIANS("§f§lCivils survivants"),
    WIN_LABEL_AXE_AND_CIVILS("§8§lAxe §7+ §f§lCivils"),
    WIN_LABEL_RESISTANCE_AND_CIVILS("§9§lRésistance §7+ §f§lCivils"),

    WIN_DETAIL_AXE("§8§l39-45 §8│ §7L'Axe l'emporte : la Résistance a été neutralisée."),
    WIN_DETAIL_RESISTANCE("§8§l39-45 §8│ §7La Résistance l'emporte : l'Axe a été neutralisé."),
    WIN_DETAIL_CIVILIANS("§8§l39-45 §8│ §7Les civils survivants accomplissent leur objectif personnel."),
    WIN_DETAIL_AXE_AND_CIVILS("§8§l39-45 §8│ §7L'Axe l'emporte. Les civils survivants gagnent aussi personnellement."),
    WIN_DETAIL_RESISTANCE_AND_CIVILS("§8§l39-45 §8│ §7La Résistance l'emporte. Les civils survivants gagnent aussi personnellement."),

    KNOWLEDGE_AXE_CORE_HEADER("§8Noyau de l'Axe"),
    KNOWLEDGE_CELL_HEADER("§9Ta cellule"),
    KNOWLEDGE_CONTACTS_HEADER("§9Contacts authentifiés"),
    KNOWLEDGE_AUTH_CELL_HEADER("§9Réseau %cell%"),

    FACT_AXE_CORE("§8§l39-45 §8│ §7Tu fais partie du §8noyau de l'Axe§7. Tes camarades connus sont personnels : le camp entier n'est pas affiché aux isolés."),
    FACT_AXE_INFILTRE_EXISTS("§8§l39-45 §8│ §7Un §8Infiltré §7opère à l'écart. Tu n'en connais pas l'identité."),
    FACT_AXE_ISOLATED("§8§l39-45 §8│ §7Tu es §8isolé du noyau§7. Tes camarades de l'Axe ne te connaissent pas."),
    FACT_RESISTANCE_FRAGMENTED("§9§l39-45 §8│ §7D'autres réseaux existent, mais tu n'en connais pas les membres. Authentifie-les : §f/role auth§7."),
    FACT_RESISTANCE_ISOLATE("§9§l39-45 §8│ §7Tu n'as pas de cellule. Les autres réseaux existent ; à toi de les trouver."),

    CONTACTS_HEADER("§8§l39-45 §8│ §fConnaissances officielles"),
    CONTACTS_NONE("§8│ §7Aucun allié officiel pour l'instant."),
    CONTACTS_OWN_CELL("§8│ §7Cellule §9%cell% §8(§7%size% membres§8)"),
    CONTACTS_OWN_ISOLATE("§8│ §7Agent isolé — pas de camarades de cellule."),
    CONTACTS_AUTH_LINE("§8│ §7Réseau §9%cell% §8: %state%"),
    CONTACTS_INTERCEPTED("§8│ §7Fragments interceptés : §f%signals%"),

    CAMP_UNKNOWN("§8§l39-45 §8│ §7Aucun camp connu."),
    CAMP_AXE_CORE("§8§l39-45 §8│ §7Camp §8Axe §7(noyau). Tes alliés connus : §f/contacts §7— pas de liste globale au-delà du noyau."),
    CAMP_AXE_ISOLATED("§8§l39-45 §8│ §7Camp §8Axe§7, isolé. Le noyau ne te connaît pas. Pas de roster."),
    CAMP_RESISTANCE("§8§l39-45 §8│ §7Camp §9Résistance§7. Le camp n'est pas une équipe unique."),
    CAMP_RESISTANCE_CELL("§8§l39-45 §8│ §7Camp §9Résistance§7, cellule §9%cell%§7. Les autres réseaux restent inconnus tant qu'ils ne sont pas authentifiés."),
    CAMP_RESISTANCE_ISOLATE("§8§l39-45 §8│ §7Camp §9Résistance§7, agent isolé. Aucune liste de camp."),
    CAMP_CIVILIAN("§8§l39-45 §8│ §7Camp §fCivils§7. Indépendant : pas d'alliés de camp, pas de roster."),

    AUTH_USAGE("§9§l39-45 §8│ §7Usage : §f/role auth <mot> [secret]"),
    AUTH_NOT_RESISTANCE("§9§l39-45 §8│ §7Cette commande est réservée à la Résistance."),
    AUTH_UNKNOWN("§9§l39-45 §8│ §7Aucun réseau ne correspond. L'indice est incomplet, faux, ou tu te fais duper."),
    AUTH_OWN_CELL("§9§l39-45 §8│ §7C'est le mot de §9ta §7cellule. Il sert à te faire reconnaître, pas à t'authentifier toi-même."),
    AUTH_ALREADY("§9§l39-45 §8│ §7Le réseau §9%cell% §7est déjà authentifié."),
    AUTH_PARTIAL("§9§l39-45 §8│ §7Indice reconnu, mais §cincomplet§7. Il manque le mot secret — et même un secret peut être volé."),
    AUTH_SECRET_MISMATCH("§9§l39-45 §8│ §7L'indice existe, le second mot ne colle pas. Doute : mauvais secret, ou usurpation."),
    AUTH_PENDING("§9§l39-45 §8│ §7Code complet présenté au réseau §9%cell%§7. Il doit encore §f/role confirmer <toi>§7."),
    AUTH_PENDING_NOTIFY("§9§l39-45 §8│ §f%player% §7présente le mot secret de votre réseau. Un contact peut §f/role confirmer %player%§7 — vérifiez d'abord."),
    AUTH_CONFIRMED("§9§l39-45 §8│ §7Liaison confirmée avec le réseau §9%cell%§7."),
    AUTH_CONFIRM_DENIED("§9§l39-45 §8│ §7Seul le chef de réseau (ou un membre si le chef est hors-jeu) peut confirmer."),
    AUTH_CONFIRM_SAME_CELL("§9§l39-45 §8│ §7Ce joueur est déjà dans ta cellule."),
    AUTH_CONFIRM_NO_PENDING("§9§l39-45 §8│ §7Aucune demande d'authentification en cours pour ce joueur vers ta cellule."),
    AUTH_PLAYER_NOT_FOUND("§9§l39-45 §8│ §7Joueur introuvable ou hors-jeu."),
    AUTH_LINKED("§9§l39-45 §8│ §7Réseau §9%cell% §7authentifié. Contacts : §f%members%"),

    AUTH_STATE_NONE("§8aucune"),
    AUTH_STATE_PARTIAL("§eindice incomplet"),
    AUTH_STATE_PENDING("§6en attente de confirmation"),
    AUTH_STATE_AUTHENTICATED("§averifié"),
    AUTH_STATE_SUSPECTED("§csuspect (indice usurpé)"),

    CODE_SIGNAL("§9§l39-45 §8│ §7Cellule §9%cell% §7— mot de reconnaissance : §f%signal% §8(incomplet, à échanger avec prudence)"),
    CODE_SECRET("§9§l39-45 §8│ §7Mot secret : §f%secret% §8(ne le donne pas avec le mot de reconnaissance à n'importe qui)"),
    CODE_SECRET_HIDDEN("§9§l39-45 §8│ §7Le mot secret est détenu par le chef de réseau."),
    CODE_ISOLATE("§9§l39-45 §8│ §7Agent isolé — mot de reconnaissance : §f%signal%"),

    INFIL_NOT_ROLE("§8§l39-45 §8│ §7Cette commande est réservée à l'Infiltré."),
    INFIL_INTERCEPT_START("§8§l39-45 §8│ §7Écoute active §f%seconds%s§7. Tu capteras un §ffragment§7 d'auth, jamais une liste de camp."),
    INFIL_INTERCEPT_HIT("§8§l39-45 §8│ §7Fragment intercepté : §f%signal% §8(incomplet). §7/role usurper %signal%"),
    INFIL_SPOOF_USAGE("§8§l39-45 §8│ §7Usage : §f/role usurper <mot intercepté>"),
    INFIL_SPOOF_NO_FRAGMENT("§8§l39-45 §8│ §7Tu n'as pas ce fragment. Intercepte d'abord."),
    INFIL_SPOOF_OK("§8§l39-45 §8│ §7Tu usurpes un indice du réseau §9%cell%§7. Tu n'en obtiens §cpas §7les membres."),
    INFIL_SPOOF_NOTIFY("§9§l39-45 §8│ §cUn indice de votre réseau circule. Quelqu'un peut se faire passer pour un contact."),

    ABILITY_CODE_DESC("§7Affiche le mot de reconnaissance (et le secret si tu es chef ou isolé). §f/role code"),
    ABILITY_AUTH_DESC("§7Présente un mot entendu en jeu. Un seul mot = doute. Mot + secret = en attente de confirmation. §f/role auth <mot> [secret]"),
    ABILITY_CONFIRM_DESC("§7Valide une liaison vers ta cellule après vérification humaine. §f/role confirmer <joueur>"),
    ABILITY_INTERCEPT_DESC("§7Écoute les authentifications et récupère un fragment, jamais le roster. §f/role intercepter"),
    ABILITY_SPOOF_DESC("§7Rejoue un fragment volé : le réseau est alerté, tu n'obtiens pas la liste. §f/role usurper <mot>"),

    CMD_SCENARIO_INACTIVE("§8§l39-45 §8│ §7Le scénario UHC 39-45 n'est pas actif."),
    CMD_NO_ROLE("§8§l39-45 §8│ §7Tu n'as pas encore de rôle."),

    ABILITY_DESC_COMMANDANT_ORDRE("Clic : Speed I et Résistance I pour toi et le noyau Axe dans le rayon. L'Infiltré n'est pas concerné. Épisode 2."),
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
