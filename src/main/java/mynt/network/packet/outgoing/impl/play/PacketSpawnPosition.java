package mynt.network.packet.outgoing.impl.play;

import mynt.network.packet.outgoing.PacketOutgoing;
import simplenet.packet.Packet;

public final class PacketSpawnPosition implements PacketOutgoing {

    @Override
    public void write(Packet packet) {
        packet.putLong(0);
    }

    @Override
    public int getOpcode() {
        return 0x46;
    }

}
