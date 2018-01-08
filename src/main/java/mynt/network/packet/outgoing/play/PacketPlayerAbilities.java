package mynt.network.packet.outgoing.play;

import myntnet.packet.outgoing.PacketBuilder;
import myntnet.packet.outgoing.PacketOutgoing;

public final class PacketPlayerAbilities implements PacketOutgoing {

    @Override
    public void write(PacketBuilder builder) {
        builder.putByte(0);
        builder.putFloat(1);
        builder.putFloat(1);
    }

    @Override
    public int getOpcode() {
        return 0x2C;
    }

}
