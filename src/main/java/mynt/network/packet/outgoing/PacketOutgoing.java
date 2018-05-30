package mynt.network.packet.outgoing;

import java.io.ByteArrayOutputStream;
import mynt.util.BufferUtil;
import simplenet.Client;
import simplenet.packet.Packet;

public interface PacketOutgoing {

    /**
     * Writes data to this {@link PacketOutgoing}.
     *
     * @param packet
     *      A {@link Packet} to write data to.
     */
    void write(Packet packet);

    /**
     * Gets the opcode of this {@link PacketOutgoing}.
     *
     * @return
     *      An {@code int}.
     */
    int getOpcode();

    default void write(Client... clients) {
        Packet packet = Packet.builder();
        BufferUtil.putVarInt(getOpcode(), packet);
        write(packet);
        packet.prepend(stream -> writeVarInt(packet.getSize(), stream));
        packet.write(clients);
    }

    default void writeAndFlush(Client... clients) {
        Packet packet = Packet.builder();
        BufferUtil.putVarInt(getOpcode(), packet);
        write(packet);
        packet.prepend(stream -> writeVarInt(packet.getSize(), stream));
        packet.writeAndFlush(clients);
    }

    private void writeVarInt(int value, ByteArrayOutputStream stream) {
        do {
            byte temp = (byte) (value & 0b01111111);

            if ((value >>>= 7) != 0) {
                temp |= 0b10000000;
            }

            stream.write(temp);
        } while (value != 0);
    }

}
