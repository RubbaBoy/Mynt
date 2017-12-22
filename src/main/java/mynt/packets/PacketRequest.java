package mynt.packets;

import myntnet.client.Client;
import myntnet.packet.incoming.impl.PacketRaw;
import myntnet.packet.outgoing.OutgoingPacket;
import mynt.StatusResponse;

import static util.GsonUtil.GSON;

public class PacketRequest implements PacketRaw {

    @Override
    public void read(Client client) {
        String string = GSON.toJson(new StatusResponse());

        new OutgoingPacket(0).putString(string).send(client);
    }

}
