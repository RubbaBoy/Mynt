package mynt;

import mynt.network.packet.incoming.handshaking.PacketLegacyPing;
import mynt.network.packet.incoming.login.PacketEncryptionResponse;
import mynt.network.packet.incoming.login.PacketLoginStart;
import myntnet.client.Client;
import myntnet.server.Server;
import mynt.network.packet.incoming.handshaking.PacketHandshake;
import mynt.network.packet.incoming.status.PacketPing;
import mynt.network.packet.incoming.status.PacketStatusRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static myntlogger.Logger.getLogger;
import static myntnet.client.State.HANDSHAKING;
import static myntnet.client.State.LOGIN;
import static myntnet.client.State.STATUS;

public class MinecraftServer {

    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private static final Set<Client> CLIENTS = new HashSet<>();

    private static Server server;

    public static void main(String[] args) {
        server = new Server();

        server.onConnect(client -> client.setServer(server));
        server.onConnect(client -> getLogger().info(client + " connected successfully!"));
        server.onConnect(CLIENTS::add);

        server.onDisconnect(CLIENTS::remove);

        getLogger().warn("TEST");
        getLogger().error("TEST");

        server.register(HANDSHAKING, 0, new PacketHandshake());
        server.register(HANDSHAKING, 0xFE, new PacketLegacyPing());
        server.register(STATUS, 0, new PacketStatusRequest());
        server.register(STATUS, 1, new PacketPing());
        server.register(LOGIN, 0, new PacketLoginStart());
        server.register(LOGIN, 1, new PacketEncryptionResponse());

        server.bind("localhost", 25565);

        EXECUTOR.scheduleAtFixedRate(() -> {
            CLIENTS.forEach(Client::flushOutgoingPackets);
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public static Server getServer() {
        return server;
    }

}
