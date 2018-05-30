package mynt.network.packet.outgoing.impl.play;

import mynt.network.packet.outgoing.PacketOutgoing;
import simplenet.packet.Packet;

public final class PacketPlayerAbilities implements PacketOutgoing {

    @Override
    public void write(Packet packet) {
        packet.putByte(0)
              .putFloat(1)
              .putFloat(1);
    }

    @Override
    public int getOpcode() {
        return 0x2C;
    }

}
