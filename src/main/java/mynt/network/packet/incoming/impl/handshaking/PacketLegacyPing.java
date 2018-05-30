package mynt.network.packet.incoming.impl.handshaking;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.util.BufferUtil;
import simplenet.packet.Packet;
import static myntlogger.Logger.getLogger;

public class PacketLegacyPing implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
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

        // Packet ID 0xFA
        client.readByte(getLogger()::info);

        // MC|PingHost
        BufferUtil.readStringUTF16(client, StandardCharsets.UTF_16BE, getLogger()::info);

        // Length of remaining data
        client.readShort(getLogger()::info);

        // Protocol
        client.readByte(getLogger()::info);

        // Hostname
        BufferUtil.readStringUTF16(client, StandardCharsets.UTF_16BE, getLogger()::info);

        // Port
        client.readInt(getLogger()::info);

        StringBuilder sb = new StringBuilder();

        sb.append('\u00A7').append(1).append('\0');
        sb.append(47).append('\0');
        sb.append("1.12.2").append('\0');
        sb.append("A Minecraft Server").append('\0');
        sb.append(0).append('\0');
        sb.append(20);

        Packet.builder()
              .putByte((byte) 0xFF)
              .putShort((short) sb.length())
              .putBytes(sb.toString().getBytes(StandardCharsets.UTF_16BE))
              .writeAndFlush(client);
    }

}
