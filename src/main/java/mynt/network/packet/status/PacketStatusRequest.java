package mynt.network.packet.status;

import myntnet.client.Client;
import myntnet.packet.incoming.impl.PacketRaw;
import myntnet.packet.outgoing.OutgoingPacket;

import static util.GsonUtil.GSON;

public class PacketStatusRequest implements PacketRaw {

    @Override
    public void read(Client client) {
        String string = GSON.toJson(new StatusResponse());

        new OutgoingPacket(0).putString(string).send(client);
    }

}
