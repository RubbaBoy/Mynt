package mynt;

import mynt.network.packet.handshaking.PacketLegacyPing;
import mynt.network.packet.login.PacketEncryptionResponse;
import mynt.network.packet.login.PacketLoginStart;
import myntnet.server.Server;
import mynt.network.packet.handshaking.PacketHandshake;
import mynt.network.packet.status.PacketPing;
import mynt.network.packet.status.PacketStatusRequest;

import static myntlogger.Logger.getLogger;
import static myntnet.client.State.HANDSHAKING;
import static myntnet.client.State.LOGIN;
import static myntnet.client.State.STATUS;

public class MinecraftServer {

    private static Server server;

    public static void main(String[] args) {
        server = new Server();

        server.onConnect(client -> getLogger().info(client + " connected successfully!"));

        getLogger().warn("TEST");
        getLogger().error("TEST");

        server.register(HANDSHAKING, 0, new PacketHandshake());
        server.register(HANDSHAKING, 0xFE, new PacketLegacyPing());
        server.register(STATUS, 0, new PacketStatusRequest());
        server.register(STATUS, 1, new PacketPing());
        server.register(LOGIN, 0, new PacketLoginStart());
        server.register(LOGIN, 1, new PacketEncryptionResponse());

        server.bind("localhost", 25565);

        while (true) {
            Thread.onSpinWait();
        }
    }

    public static Server getServer() {
        return server;
    }

}
