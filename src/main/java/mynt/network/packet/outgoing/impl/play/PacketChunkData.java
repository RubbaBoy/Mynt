package mynt.network.packet.outgoing.impl.play;

import mynt.network.packet.outgoing.PacketOutgoing;
import simplenet.packet.Packet;

public final class PacketChunkData implements PacketOutgoing {

    @Override
    public void write(Packet packet) {

    }

    @Override
    public int getOpcode() {
        return 0x20;
    }

}
