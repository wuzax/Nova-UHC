package net.novaproject.lgbridge;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BungeeConnectTest {

    @Test
    void encodesConnectSubchannelAndConfiguredServer() throws IOException {
        byte[] payload = BungeeConnect.connectPayload("lg");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(payload));
        assertEquals(BungeeConnect.SUBCHANNEL, in.readUTF());
        assertEquals("lg", in.readUTF());
        assertEquals(-1, in.read());
    }

    @Test
    void trimsServerName() throws IOException {
        byte[] payload = BungeeConnect.connectPayload("  lg-uhc  ");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(payload));
        assertEquals("Connect", in.readUTF());
        assertEquals("lg-uhc", in.readUTF());
    }

    @Test
    void rejectsBlankServerName() {
        assertThrows(IllegalArgumentException.class, () -> BungeeConnect.connectPayload("  "));
        assertThrows(IllegalArgumentException.class, () -> BungeeConnect.connectPayload(null));
    }
}
