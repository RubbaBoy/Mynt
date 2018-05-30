package mynt.network.packet.outgoing.impl.play;

import mynt.attribute.Location;
import mynt.entity.living.player.Player;
import mynt.network.packet.outgoing.PacketOutgoing;
import mynt.util.BufferUtil;
import simplenet.packet.Packet;

public final class PacketPlayerPositionAndLook implements PacketOutgoing {

    private final Player player;

    public PacketPlayerPositionAndLook(Player player) {
        this.player = player;
    }

    @Override
    public void write(Packet packet) {
        Location location = player.getLocation();

        packet.putDouble(location.getX())
              .putDouble(location.getY())
              .putDouble(location.getZ())
              .putFloat(0)
              .putFloat(0)
              .putByte(0);

        BufferUtil.putVarInt(player.getId(), packet);
    }

    @Override
    public int getOpcode() {
        return 0x2F;
    }

}
