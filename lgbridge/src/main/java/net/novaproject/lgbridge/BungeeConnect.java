package net.novaproject.lgbridge;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class BungeeConnect {

    public static final String CHANNEL = "BungeeCord";
    public static final String SUBCHANNEL = "Connect";

    private BungeeConnect() {
    }

    public static byte[] connectPayload(String serverName) {
        String name = requireServerName(serverName);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        try {
            out.writeUTF(SUBCHANNEL);
            out.writeUTF(name);
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible d'encoder BungeeCord Connect", e);
        }
        return bytes.toByteArray();
    }

    static String requireServerName(String serverName) {
        if (serverName == null || serverName.isBlank()) {
            throw new IllegalArgumentException("lg-server-name est vide");
        }
        return serverName.trim();
    }
}
