package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.Lang;

import java.util.Map;

public enum Lang3945 implements Lang {

    SCENARIO_DESC("§8UHC 39-45 §7est un UHC à rôles cachés sur le thème de la Seconde Guerre mondiale. "
            + "Trois camps : §8§lAxe §7(organisé), §9§lRésistance §7(cellules fragmentées) et §f§lCivils §7(indépendants). "
            + "La boucle UHC classique (bordure, épisodes, meetup) reste celle de Nova. "
            + "Pouvoirs, authentification des cellules et infiltration arriveront plus tard."),

    ROLE_DESC_COMMANDANT("\n  §8§lCOMMANDANT\n"
            + "  §7Camp §8Axe\n"
            + "  §8│ §7Officier unique de l'Axe. Le camp se connaît.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_SOLDAT("\n  §8§lSOLDAT\n"
            + "  §7Camp §8Axe §7(rôle de masse)\n"
            + "  §8│ §7Combattant de l'Axe. Connaît ses camarades.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_CHEF_RESEAU("\n  §9§lCHEF DE RÉSEAU\n"
            + "  §7Camp §9Résistance\n"
            + "  §8│ §7Organise une cellule. Authentification des cellules : §cà venir\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_RESISTANT("\n  §9§lRÉSISTANT\n"
            + "  §7Camp §9Résistance §7(rôle de masse)\n"
            + "  §8│ §7Membre d'une cellule fragmentée. Pas de visibilité globale du camp.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_CIVIL("\n  §f§lCIVIL\n"
            + "  §7Camp §fCivils §7(indépendant)\n"
            + "  §8│ §7Survit pour lui-même. Victoire solo.\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),

    ROLE_DESC_INFORMATEUR("\n  §f§lINFORMATEUR\n"
            + "  §7Camp §fCivils\n"
            + "  §8│ §7Rôle unique. Infiltration et connaissances asymétriques : §cà venir\n"
            + "  §8│ §7Pouvoirs : §cà venir\n"),
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
