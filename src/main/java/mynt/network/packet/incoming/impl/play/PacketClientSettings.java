package mynt.network.packet.incoming.impl.play;

import mynt.entity.living.player.Player;
import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.network.packet.outgoing.impl.play.PacketPlayerPositionAndLook;
import mynt.util.BufferUtil;
import static myntlogger.Logger.getLogger;

public final class PacketClientSettings implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        BufferUtil.readString(client, locale -> {
            getLogger().info("LOCALE: " + locale);

            client.readByte(viewDistance -> {
                getLogger().info("VIEW DISTANCE: " + viewDistance);

                BufferUtil.readVarInt(client, chatMode -> {
                    getLogger().info("CHAT MODE: " + chatMode);

                    BufferUtil.readBoolean(client, chatColors -> {
                        getLogger().info("CHAT COLORS: " + chatColors);

                        client.readByte(displayedSkinParts -> {
                            getLogger().info("DISPLAYED SKIN PARTS: " + displayedSkinParts);

                            BufferUtil.readVarInt(client, mainHand -> {
                                getLogger().info("MAIN HAND: " + mainHand);

                                new PacketPlayerPositionAndLook(new Player(client)).writeAndFlush(client);
                            });
                        });
                    });
                });
            });
        });
    }

}
