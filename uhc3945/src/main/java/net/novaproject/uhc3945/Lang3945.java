package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.Lang;

import java.util.Map;

public enum Lang3945 implements Lang {

    SCENARIO_DESC("§8UHC 39-45 §7est un UHC à rôles cachés sur le thème de la Seconde Guerre mondiale. "
            + "Trois camps : §8§lAxe §7(organisé), §9§lRésistance §7(cellules fragmentées) et §f§lCivils §7(indépendants). "
            + "L'Axe gagne en neutralisant la Résistance, la Résistance en neutralisant l'Axe. "
            + "Les civils ont un objectif personnel et ne bloquent pas la victoire militaire. "
            + "La boucle UHC classique (bordure, épisodes, meetup) reste celle de Nova. "
            + "Avant le meetup, les regroupements sont limités : §8Axe 4§7, §9Résistance 3§7, §fCivils 3§7."),

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

    ROLE_DESC_COMMANDANT("\n  §8§lCOMMANDANT\n"
            + "  §7Camp §8Axe\n"
            + "  §8│ §7Officier unique de l'Axe. Le camp se connaît.\n"
            + "  §8│ §7Victoire : §8neutraliser la Résistance §7et survivre avec l'Axe.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_SOLDAT("\n  §8§lSOLDAT\n"
            + "  §7Camp §8Axe §7(rôle de masse)\n"
            + "  §8│ §7Combattant de l'Axe. Connaît ses camarades.\n"
            + "  §8│ §7Victoire : §8neutraliser la Résistance §7et survivre avec l'Axe.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_CHEF_RESEAU("\n  §9§lCHEF DE RÉSEAU\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Organise une cellule. Authentification des cellules : §cà venir\n"
            + "  §8│ §7Victoire : §9neutraliser l'Axe §7et survivre avec la Résistance.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_RESISTANT("\n  §9§lRÉSISTANT\n"
            + "  §7Camp §9Résistance §7(rôle de masse)\n"
            + "  §8│ §7Membre d'une cellule fragmentée. Pas de visibilité globale du camp.\n"
            + "  §8│ §7Victoire : §9neutraliser l'Axe §7et survivre avec la Résistance.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_CIVIL("\n  §f§lCIVIL\n"
            + "  §7Camp §fCivils §7(indépendant)\n"
            + "  §8│ §7Objectif personnel : §fsurvivre.\n"
            + "  §8│ §7Tu gagnes si tu es encore en vie à la victoire militaire, ou s'il ne reste que des civils.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_INFORMATEUR("\n  §f§lINFORMATEUR\n"
            + "  §7Camp §fCivils\n"
            + "  §8│ §7Objectif personnel : §fsurvivre §7(transmettre un renseignement : hook à venir).\n"
            + "  §8│ §7Tu gagnes personnellement, indépendamment du camp militaire vainqueur.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_PERSONAL_OBJECTIVE("  §8│ §7Objectif personnel : §f%objective%"),

    OBJ_SURVIVE("survivre jusqu'à la fin"),
    OBJ_EXFILTRATE("s'exfiltrer (hook)"),
    OBJ_INFORM("survivre / transmettre un renseignement (hook)"),

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
