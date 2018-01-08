package mynt.network.packet.outgoing.status;

import mynt.network.packet.incoming.status.StatusResponse;
import myntnet.packet.outgoing.PacketBuilder;
import myntnet.packet.outgoing.PacketOutgoing;

import static mynt.util.GsonUtil.GSON;

public final class PacketResponse implements PacketOutgoing {

    private final String response;

    public PacketResponse(StatusResponse response) {
        this.response = GSON.toJson(response);
    }

    @Override
    public void write(PacketBuilder builder) {
        builder.putString(response);
    }

    @Override
    public int getOpcode() {
        return 0;
    }

}
