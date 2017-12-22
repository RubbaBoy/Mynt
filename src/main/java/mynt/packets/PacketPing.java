package mynt.packets;

import myntnet.client.Client;
import myntnet.packet.incoming.impl.PacketReadable;
import myntnet.packet.outgoing.OutgoingPacket;

import java.nio.ByteBuffer;

public final class PacketPing implements PacketReadable {

    @Override
    public void read(Client client, ByteBuffer buffer) {
        long number = buffer.getLong();

        System.out.println("RECEIVED: " + number);

        new OutgoingPacket(1).putLong(number).send(client, Client::close);
    }

}
