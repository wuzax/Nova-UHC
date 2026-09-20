package net.novaproject.lgbridge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LgBridgeSettingsTest {

    @Test
    void substitutesPrefixAndServerInFrenchMessages() {
        LgBridgeSettings settings = new LgBridgeSettings(
                "lg",
                true,
                true,
                "§5§lLG-UHC §8│",
                "%prefix% §fMode activé vers §d%server%§f.",
                "%prefix% §fLancement vers §d%server%§f.",
                "%prefix% §7Connexion (§d%server%§7)…",
                "%prefix% §cAucun joueur.",
                "%prefix% §7Désactivé.",
                "%prefix% §aRechargé : §d%server%§a.");

        assertEquals("lg", settings.serverName());
        assertTrue(settings.transferOnSelect());
        assertTrue(settings.transferOnStart());
        assertEquals("§5§lLG-UHC §8│ §fMode activé vers §dlg§f.", settings.selectedMessage());
        assertEquals("§5§lLG-UHC §8│ §fLancement vers §dlg§f.", settings.startingMessage());
        assertEquals("§5§lLG-UHC §8│ §7Connexion (§dlg§7)…", settings.transferMessage());
        assertEquals("§5§lLG-UHC §8│ §cAucun joueur.", settings.emptyMessage());
        assertEquals("§5§lLG-UHC §8│ §7Désactivé.", settings.disabledMessage());
        assertEquals("§5§lLG-UHC §8│ §aRechargé : §dlg§a.", settings.reloadedMessage());
    }
}
