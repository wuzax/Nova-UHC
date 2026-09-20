package net.novaproject.lguhc;

import net.novaproject.novauhc.lang.Lang;

import java.util.Map;

public enum LangLguhc implements Lang {

    SCENARIO_DESC("§aLG-UHC §7est un UHC à rôles cachés inspiré de Ph1Lou (AGPL). "
            + "Camps : §aVillage§7, §cLoups§7, §eSolitaires§7. "
            + "Les loups se connaissent et parlent avec le préfixe §flg §7. "
            + "Victoire village si plus aucun loup, victoire loups si plus aucun villageois. "
            + "Le couple de Cupidon gagne ensemble. "
            + "§cPas le plugin WereWolf 1.21§7 : rôles réimplémentés sur Nova 1.8.8."),

    ROLE_DESC_VILLAGEOIS("\n  §a§lVILLAGEOIS\n  §7Camp Village. Aucun pouvoir. Victoire avec le village."),
    ROLE_DESC_LOUP("\n  §c§lLOUP-GAROU\n  §7Camp Loups. Connaît ses loups. Chat §flg <msg>§7. Victoire en éliminant le village."),
    ROLE_DESC_VOYANTE("\n  §a§lVOYANTE\n  §7§f/role voir <joueur>§7 : révèle le rôle (1×)."),
    ROLE_DESC_SORCIERE("\n  §a§lSORCIÈRE\n  §7§f/role soigner <joueur>§7 et §f/role tuer <joueur>§7 : une fois chacun."),
    ROLE_DESC_CHASSEUR("\n  §a§lCHASSEUR\n  §7À sa mort, tue son assassin s'il est encore en jeu."),
    ROLE_DESC_PETITE_FILLE("\n  §a§lPETITE FILLE\n  §7Entend le chat des loups (§flg§7). Vision nocturne."),
    ROLE_DESC_CUPIDON("\n  §a§lCUPIDON\n  §7§f/role couple <j1> <j2>§7 : lie deux joueurs (meurent et gagnent ensemble)."),
    ROLE_DESC_ANCIEN("\n  §a§lANCIEN\n  §7Survive une première mort (résurrection unique)."),

    POWER_PLAYER_NOT_FOUND("§a§lLG-UHC §8│ §7Joueur introuvable ou hors-jeu."),
    POWER_USAGE("§a§lLG-UHC §8│ §7Usage : §f%usage%"),
    POWER_SELF("§a§lLG-UHC §8│ §7Choisis un autre joueur."),

    MSG_VOYANTE("§a§lLG-UHC §8│ §f%target% §7est §f%role%§7."),
    MSG_SORCIERE_HEAL("§a§lLG-UHC §8│ §7Tu soignes §f%target%§7."),
    MSG_SORCIERE_KILL("§c§lLG-UHC §8│ §7Tu consumes ta potion de mort sur §f%target%§7."),
    MSG_CUPIDON_OK("§d§lLG-UHC §8│ §7§f%a% §7et §f%b% §7sont désormais un couple."),
    MSG_CUPIDON_ALREADY("§a§lLG-UHC §8│ §7Le couple est déjà formé."),
    MSG_ANCIEN_REVIVE("§a§lLG-UHC §8│ §7L'Ancien revient d'entre les morts. Plus de seconde chance."),
    MSG_CHASSEUR_SHOT("§6§lLG-UHC §8│ §7Le Chasseur emporte §f%target% §7dans sa mort."),
    MSG_WOLF_CHAT("§a§lLG-UHC §8│ §7Chat des loups : commence tes messages par §flg §7."),

    ABILITY_VOIR("Révéler le rôle d'un joueur, une fois."),
    ABILITY_SOIGNER("Potion de vie : soigne complètement, une fois."),
    ABILITY_TUER("Potion de mort : tue la cible, une fois."),
    ABILITY_COUPLE("Désigner les deux amoureux."),
    ABILITY_NIGHT("Force et vision la nuit (loups)."),
    ABILITY_LISTEN("Écoute le chat des loups."),
    ABILITY_SHOT("Tir mortel sur l'assassin."),
    ABILITY_REVIVE("Résurrection unique.");

    private final Map<String, String> translations;

    LangLguhc(String fr) {
        this.translations = Map.of("fr_FR", fr);
    }

    @Override
    public String getKey() {
        return "lguhc." + name();
    }

    @Override
    public Map<String, String> getTranslations() {
        return translations;
    }
}
