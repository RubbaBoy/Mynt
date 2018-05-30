package mynt.network.packet.incoming.impl.play;

import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.util.BufferUtil;

import static myntlogger.Logger.getLogger;

public final class PacketPluginMessage implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        BufferUtil.readString(client, s -> {
            switch (s) {
                case "MC|Brand":
                    BufferUtil.readString(client, getLogger()::info);
                    break;
                default:
                    /*TODO: Because buffers are reused, this could break the server.
                    byte[] b = new byte[buffer.remaining()];
                    buffer.get(b);*/
                    getLogger().warn("Unhandled Plugin Message: " + s);
            }
        });
    }

}
