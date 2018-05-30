package mynt.network.packet.incoming.impl.play;

import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.util.BufferUtil;

import static myntlogger.Logger.getLogger;

public final class PacketTeleportConfirm implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        BufferUtil.readVarInt(client, teleportId -> {
            getLogger().info("Teleport ID: " + teleportId);
        });
    }

}
