package mynt.network.packet.incoming.handshaking;

import myntnet.client.Client;
import myntnet.packet.incoming.impl.PacketReadable;
import myntnet.util.BufferUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static myntlogger.Logger.getLogger;

public class PacketLegacyPing implements PacketReadable {

    @Override
    public void read(Client client, ByteBuffer buffer) {
        /*
         * We are unable to read this byte, as it
         * has already been read by MyntNet; the
         * opcode of this packet is 0xFE (254), so
         * reading the "length" required two bytes.
         *
         * This byte, if it were to be read, would
         * always be 1.  Because of this, changing
         * the way packets are read isn't required.
         */
        // getLogger().info(buffer.get());

        getLogger().info(buffer.get()); // Packet ID 0xFA
        getLogger().info(BufferUtil.getStringUTF16(buffer, StandardCharsets.UTF_16BE)); // MC|PingHost
        getLogger().info(buffer.getShort()); // Length of remaining data
        getLogger().info(buffer.get()); // Protocol
        getLogger().info(BufferUtil.getStringUTF16(buffer, StandardCharsets.UTF_16BE)); // Hostname
        getLogger().info(buffer.getInt()); // Port

        StringBuilder sb = new StringBuilder();

        sb.append('\u00A7').append(1).append('\0');
        sb.append(47).append('\0');
        sb.append("1.12.2").append('\0');
        sb.append("A Minecraft Server").append('\0');
        sb.append(0).append('\0');
        sb.append(20);

        ByteBuffer payload = ByteBuffer.allocate(3 + sb.length() * 2);

        payload.put((byte) 0xFF);
        payload.putShort((short) sb.length());
        payload.put(sb.toString().getBytes(StandardCharsets.UTF_16BE));

        client.getChannel().write(payload.flip());
    }

}
