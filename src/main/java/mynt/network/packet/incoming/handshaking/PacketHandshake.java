package mynt.network.packet.incoming.handshaking;

import myntnet.client.Client;
import myntnet.client.State;
import myntnet.packet.incoming.impl.PacketReadable;
import myntnet.util.BufferUtil;

import java.nio.ByteBuffer;

public class PacketHandshake implements PacketReadable {

    @Override
    public void read(Client client, ByteBuffer buffer) {
        int version = BufferUtil.getVarInt(buffer);
        String address = BufferUtil.getString(buffer);
        int port = BufferUtil.getUnsignedShort(buffer);
        State state = State.values()[BufferUtil.getVarInt(buffer)];

        System.out.println("VERSION: " + version);
        System.out.println("ADDRESS: " + address);
        System.out.println("PORT: " + port);
        System.out.println("STATE: " + state);

        client.setState(state);
    }

}
