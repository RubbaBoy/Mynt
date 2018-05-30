package mynt.network.packet.outgoing.impl.play;

import mynt.network.MyntClient;
import mynt.network.packet.outgoing.PacketOutgoing;
import simplenet.packet.Packet;

public final class PacketKeepAlive implements PacketOutgoing {

    private final MyntClient client;

    public PacketKeepAlive(MyntClient client) {
        this.client = client;
    }

    @Override
    public void write(Packet packet) {
        long keepAliveID = System.currentTimeMillis();

        client.getKeepAlivePackets().offer(keepAliveID);

        packet.putLong(keepAliveID);
    }

    @Override
    public int getOpcode() {
        return 0x1F;
    }

}
