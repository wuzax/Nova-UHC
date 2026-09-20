package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.Lang;

import java.util.Map;

public enum Lang3945 implements Lang {

    SCENARIO_DESC("§8UHC 39-45 §7est un UHC à rôles cachés. "
            + "Trois camps : §8§lAxe §7(noyau organisé + Infiltré isolé), "
            + "§9§lRésistance §7(cellules de 2-3, authentification) et §f§lCivils §7(indépendants). "
            + "La boucle UHC (bordure, épisodes, meetup) reste celle de Nova. "
            + "Commandes : §f/role§7, §f/contacts§7, §f/camp§7, §f/role code§7, §f/role auth§7."),

    ROLE_DESC_COMMANDANT("\n  §8§lCOMMANDANT\n"
            + "  §7Camp §8Axe\n"
            + "  §8│ §7Officier du noyau. Connaît les Soldats, pas l'Infiltré.\n"
            + "  §8│ §7Pouvoirs de combat : §cà venir\n"),

    ROLE_DESC_SOLDAT("\n  §8§lSOLDAT\n"
            + "  §7Camp §8Axe §7(rôle de masse)\n"
            + "  §8│ §7Membre du noyau. Connaît le Commandant et les autres Soldats.\n"
            + "  §8│ §7Pouvoirs de combat : §cà venir\n"),

    ROLE_DESC_INFILTRE("\n  §8§lINFILTRÉ\n"
            + "  §7Camp §8Axe §7(isolé du noyau)\n"
            + "  §8│ §7N'apparaît pas sur la liste des alliés de l'Axe.\n"
            + "  §8│ §7§f/role intercepter §7puis §f/role usurper <mot> §7: fragment d'auth, jamais le roster.\n"),

    ROLE_DESC_CHEF_RESEAU("\n  §9§lCHEF DE RÉSEAU\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Connaît sa cellule. Détient le mot secret. Valide les liaisons.\n"
            + "  §8│ §7§f/role code§7, §f/role auth <mot> [secret]§7, §f/role confirmer <joueur>\n"),

    ROLE_DESC_RESISTANT("\n  §9§lRÉSISTANT\n"
            + "  §7Camp §9Résistance §7(rôle de masse)\n"
            + "  §8│ §7Connaît uniquement sa cellule. Les autres réseaux restent à authentifier.\n"
            + "  §8│ §7§f/role code§7, §f/role auth <mot> [secret]§7, §f/role confirmer <joueur>\n"),

    ROLE_DESC_CIVIL("\n  §f§lCIVIL\n"
            + "  §7Camp §fCivils §7(indépendant)\n"
            + "  §8│ §7Aucune connaissance de camp. Victoire solo.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_INFORMATEUR("\n  §f§lINFORMATEUR\n"
            + "  §7Camp §fCivils\n"
            + "  §8│ §7Rôle unique, indépendant. Pas de roster de camp.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_CARD_CELL("  §8│ §7Ta cellule : §9%cell%"),
    ROLE_CARD_ISOLATE("  §8│ §7Tu es un agent isolé, sans cellule."),

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
