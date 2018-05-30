package mynt.network.packet.incoming.impl.status;

import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.network.packet.outgoing.impl.status.PacketPong;

public final class PacketPing implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        client.readLong(number -> {
            System.out.println("RECEIVED: " + number);

            new PacketPong(number).writeAndFlush(client);
        });
    }

}
