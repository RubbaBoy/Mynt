package mynt.network.packet.incoming.impl.play;

import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.util.BufferUtil;

public final class PacketPlayerPositionAndLook implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        client.readDouble(x -> {});
        client.readDouble(y -> {});
        client.readDouble(z -> {});
        client.readFloat(yaw -> {});
        client.readFloat(pitch -> {});
        BufferUtil.readBoolean(client, onGround -> {});
    }

}
