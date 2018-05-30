package mynt.network.packet.outgoing.impl.play;

import mynt.network.packet.outgoing.PacketOutgoing;
import mynt.util.BufferUtil;
import simplenet.packet.Packet;

public final class PacketJoinGame implements PacketOutgoing {

    @Override
    public void write(Packet packet) {
        packet.putInt(1)
              .putByte(1)
              .putInt(0)
              .putByte(0)
              .putByte(20);

        BufferUtil.putString("flat", packet);
        BufferUtil.putBoolean(false, packet);
    }

    @Override
    public int getOpcode() {
        return 0x23;
    }

}
