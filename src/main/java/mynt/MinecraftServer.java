package mynt;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import mynt.network.MyntClient;
import mynt.network.State;
import mynt.network.packet.incoming.impl.handshaking.PacketHandshake;
import mynt.network.packet.incoming.impl.handshaking.PacketLegacyPing;
import mynt.network.packet.incoming.impl.login.PacketEncryptionResponse;
import mynt.network.packet.incoming.impl.login.PacketLoginStart;
import mynt.network.packet.incoming.impl.play.PacketClientSettings;
import mynt.network.packet.incoming.impl.play.PacketKeepAlive;
import mynt.network.packet.incoming.impl.play.PacketPlayerPositionAndLook;
import mynt.network.packet.incoming.impl.play.PacketPluginMessage;
import mynt.network.packet.incoming.impl.play.PacketTeleportConfirm;
import mynt.network.packet.incoming.impl.status.PacketPing;
import mynt.network.packet.incoming.impl.status.PacketStatusRequest;
import mynt.util.BufferUtil;
import simplenet.Client;
import simplenet.Server;
import static myntlogger.Logger.getLogger;

public final class MinecraftServer {

    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private static final Map<AsynchronousSocketChannel, MyntClient> CLIENTS = new HashMap<>();

    private static long ticks;

    public static void main(String[] args) {
        Server server = new Server();

        server.onConnect(c -> {
            getLogger().info(c.getChannel() + " connected successfully!");

            MyntClient client = new MyntClient(c);

            BufferUtil.readVarIntAlways(client, length -> {
                System.out.println("LENGTH: " + length);

                // TODO: Why does this have to be c?
                BufferUtil.readVarInt(c, opcode -> {
                    System.out.println("OPCODE: " + opcode);

                    switch (opcode) {
                        case 0x00:
                            switch (client.getState()) {
                                case HANDSHAKING:
                                    new PacketHandshake().read(client);
                                    break;
                                case STATUS:
                                    new PacketStatusRequest().read(client);
                                    break;
                                case LOGIN:
                                    new PacketLoginStart().read(client);
                                    break;
                                case PLAY:
                                    new PacketTeleportConfirm().read(client);
                                    break;
                            }
                            break;
                        case 0x01:
                            switch (client.getState()) {
                                case STATUS:
                                    new PacketPing().read(client);
                                    break;
                                case LOGIN:
                                    new PacketEncryptionResponse().read(client);
                                    break;
                            }
                            break;
                        case 0x04:
                            if (client.getState() == State.PLAY) {
                                new PacketClientSettings().read(client);
                            }
                            break;
                        case 0x09:
                            if (client.getState() == State.PLAY) {
                                new PacketPluginMessage().read(client);
                            }
                            break;
                        case 0x0E:
                            if (client.getState() == State.PLAY) {
                                new PacketPlayerPositionAndLook().read(client);
                            }
                            break;
                        case 0x0B:
                            if (client.getState() == State.PLAY) {
                                new PacketKeepAlive().read(client);
                            }
                            break;
                        case 0xFE:
                            if (client.getState() == State.HANDSHAKING) {
                                new PacketLegacyPing().read(client);
                            }
                            break;
                    }
                });
            });
        });

        server.onDisconnect(client -> {
            getLogger().info(client + " has disconnected!");
            CLIENTS.remove(client.getChannel());
        });

        getLogger().warn("TEST");
        getLogger().error("TEST");

        server.bind("localhost", 25565);

        EXECUTOR.scheduleAtFixedRate(() -> {
            ticks++;

            // Every 30 seconds
            if (ticks % 20 * 30 == 0) {
                CLIENTS.values().forEach(client -> {
                    if (client.getKeepAlivePacketsReceived() == 0) {
                        // TODO: Kick player.
                    } else {
                        client.resetKeepAlivePacketsReceived();
                    }
                });
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public static ScheduledExecutorService getExecutor() {
        return EXECUTOR;
    }

    public static void addPlayer(MyntClient client) {
        CLIENTS.put(client.getChannel(), client);
    }

    public static Collection<MyntClient> getClients() {
        return CLIENTS.values();
    }

}
