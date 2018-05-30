package mynt.network.packet.incoming.impl.handshaking;

import mynt.network.MyntClient;
import mynt.network.State;
import mynt.network.packet.incoming.PacketIncoming;

import mynt.util.BufferUtil;

public class PacketHandshake implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        BufferUtil.readVarInt(client, version -> {
            System.out.println("VERSION: " + version);

            BufferUtil.readString(client, address -> {
                System.out.println("ADDRESS: " + address);

                client.readShort(port -> {
                    System.out.println("PORT: " + (port & 0xFFFF));

                    BufferUtil.readVarInt(client, ordinal -> {
                        State state = State.values()[ordinal];

                        System.out.println("STATE: " + state);

                        client.setState(state);
                    });
                });
            });
        });
    }

}
