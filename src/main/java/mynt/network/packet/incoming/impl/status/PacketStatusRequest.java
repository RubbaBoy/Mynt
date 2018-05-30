package mynt.network.packet.incoming.impl.status;

import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.network.packet.outgoing.impl.status.PacketResponse;

public class PacketStatusRequest implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        new PacketResponse(new StatusResponse()).writeAndFlush(client);
    }

}
