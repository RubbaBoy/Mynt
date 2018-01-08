package mynt.network.packet.outgoing.play;

import myntnet.packet.outgoing.PacketBuilder;
import myntnet.packet.outgoing.PacketOutgoing;

public final class PacketJoinGame implements PacketOutgoing {

    @Override
    public void write(PacketBuilder builder) {
        builder.putInt(1);
        builder.putByte(1);
        builder.putInt(0);
        builder.putByte(0);
        builder.putByte(20);
        builder.putString("flat");
        builder.putBoolean(false);
    }

    @Override
    public int getOpcode() {
        return 0x23;
    }

}
