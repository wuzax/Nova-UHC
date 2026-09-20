package net.novaproject.lguhc;

import net.novaproject.novauhc.lang.Lang;

import java.util.Map;

public enum LangLguhc implements Lang {

    SCENARIO_DESC("§cLG-UHC (Ph1Lou) §7n'est §cpas §7un mode Nova : les rôles restent ceux du plugin officiel "
            + "§fWereWolfPlugin §7(AGPL, Ph1Lou, Spigot §f#73113§7). "
            + "Installez §fWereWolfPlugin.jar §7dans §fplugins/§7. "
            + "§cNe lancez pas une partie Nova et une partie WereWolf en même temps. "
            + "Nova : 39-45 / Taupe / Ultimate. LG : §f/a §7et §f/ww§7."),

    DETECTED("§a§lLG-UHC §8│ §7WereWolfPlugin détecté. Composez et lancez via §f/a§7 — pas le start Nova."),

    MISSING_PLUGIN("§c§lLG-UHC §8│ §7WereWolfPlugin n'est pas chargé. "
            + "Téléchargez le JAR officiel (Spigot §f#73113§7) ou compilez Ph1Lou (JDK 8+). Voir §fLGUHC.md§7."),

    BLOCK_NOVA_START("§c§lLG-UHC §8│ §7Ce mode se joue avec §fWereWolfPlugin §7(§f/a start§7), "
            + "pas avec le démarrage Nova. Décochez LG-UHC pour lancer une UHC Nova.");

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
