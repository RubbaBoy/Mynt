package mynt.network.packet.outgoing;

import java.util.ArrayDeque;
import java.util.Deque;
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
        packet.prepend(() -> writeVarInt(packet.getSize(), packet));
        packet.write(clients);
    }

    default void writeAndFlush(Client... clients) {
        Packet packet = Packet.builder();
        BufferUtil.putVarInt(getOpcode(), packet);
        write(packet);
        packet.prepend(() -> writeVarInt(packet.getSize(), packet));
        packet.writeAndFlush(clients);
    }

    private void writeVarInt(int value, Packet packet) {
        Deque<Byte> stack = new ArrayDeque<>();

        do {
            byte temp = (byte) (value & 0b01111111);

            if ((value >>>= 7) != 0) {
                temp |= 0b10000000;
            }

            stack.push(temp);
        } while (value != 0);

        while (!stack.isEmpty()) {
            packet.putByte(stack.pop());
        }
    }

}
