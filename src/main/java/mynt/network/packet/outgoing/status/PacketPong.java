package mynt.network.packet.outgoing.status;

import myntnet.packet.outgoing.PacketBuilder;
import myntnet.packet.outgoing.PacketOutgoing;

public final class PacketPong implements PacketOutgoing {

    private final long value;

    public PacketPong(long value) {
        this.value = value;
    }

    @Override
    public void write(PacketBuilder builder) {
        builder.putLong(value);
    }

    @Override
    public int getOpcode() {
        return 1;
    }

}
