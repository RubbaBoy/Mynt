package mynt;

import myntnet.server.Server;
import mynt.packets.PacketHandshake;
import mynt.packets.PacketPing;
import mynt.packets.PacketRequest;

import static myntnet.client.State.HANDSHAKING;
import static myntnet.client.State.STATUS;

public class MinecraftServer {

    private static Server server;

    public static void main(String[] args) {
        server = new Server(client -> {
            System.out.println(client + " connected successfully!");
        });

        server.register(HANDSHAKING, 0, new PacketHandshake());
        server.register(STATUS, 0, new PacketRequest());
        server.register(STATUS, 1, new PacketPing());

        server.bind("localhost", 25565);

        while (true) {
            Thread.onSpinWait();
        }
    }

    public static Server getServer() {
        return server;
    }

}
