package mynt.network.packet.incoming.status;

import mynt.network.packet.outgoing.status.PacketPong;
import myntnet.client.Client;
import myntnet.packet.incoming.impl.PacketReadable;
import myntnet.packet.outgoing.PacketOutgoing;

import java.nio.ByteBuffer;

public final class PacketPing implements PacketReadable {

    @Override
    public void read(Client client, ByteBuffer buffer) {
        long number = buffer.getLong();

        System.out.println("RECEIVED: " + number);

        client.writeAndFlush(new PacketPong(number));
    }

}
