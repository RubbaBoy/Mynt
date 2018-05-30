package mynt.network.packet.outgoing.impl.status;

import mynt.network.packet.incoming.impl.status.StatusResponse;
import mynt.network.packet.outgoing.PacketOutgoing;
import mynt.util.BufferUtil;
import simplenet.packet.Packet;

import static mynt.util.GsonUtil.GSON;

public final class PacketResponse implements PacketOutgoing {

    private final String response;

    public PacketResponse(StatusResponse response) {
        this.response = GSON.toJson(response);
    }

    @Override
    public void write(Packet packet) {
        BufferUtil.putString(response, packet);
    }

    @Override
    public int getOpcode() {
        return 0;
    }

}
