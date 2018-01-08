package mynt.network.packet.outgoing.play;

import myntnet.packet.outgoing.PacketBuilder;
import myntnet.packet.outgoing.PacketOutgoing;

public final class PacketSpawnPosition implements PacketOutgoing {

    @Override
    public void write(PacketBuilder builder) {
        builder.putLong(0);
    }

    @Override
    public int getOpcode() {
        return 0x46;
    }

}
