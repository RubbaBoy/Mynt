package mynt.network.packet.incoming.status;

import mynt.network.packet.outgoing.status.PacketResponse;
import myntnet.client.Client;
import myntnet.packet.incoming.impl.PacketRaw;

public class PacketStatusRequest implements PacketRaw {

    @Override
    public void read(Client client) {
        client.write(new PacketResponse(new StatusResponse()));
    }

}
