package mynt.network.packet.outgoing.impl.status;

import mynt.network.packet.outgoing.PacketOutgoing;
import simplenet.packet.Packet;

public final class PacketPong implements PacketOutgoing {

    private final long value;

    public PacketPong(long value) {
        this.value = value;
    }

    @Override
    public void write(Packet packet) {
        packet.putLong(value);
    }

    @Override
    public int getOpcode() {
        return 1;
    }

}
