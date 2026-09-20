package net.novaproject.lguhc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WereWolfPresenceTest {

    @Test
    void officialPluginAndApiNamesMatchPh1Lou() {
        assertEquals("WereWolfPlugin", WereWolfPresence.PLUGIN_NAME);
        assertEquals("fr.ph1lou.werewolfapi.GetWereWolfAPI", WereWolfPresence.API_CLASS);
        assertEquals("LG-UHC (Ph1Lou)", LguhcScenario.NAME);
    }
}
