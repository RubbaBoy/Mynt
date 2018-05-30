package mynt.network.packet.incoming.impl.play;

import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;

public final class PacketKeepAlive implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        client.readLong(keepAliveID -> {
            if (keepAliveID != client.getKeepAlivePackets().poll()) {
                // TODO: Kick player.
            } else {
                client.incrementKeepAlivePacketsReceived();
            }
        });
    }

}
