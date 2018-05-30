package mynt.network.packet.incoming.impl.login;

import java.util.Arrays;
import mynt.network.MyntClient;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.network.packet.outgoing.impl.login.PacketEncryptionRequest;
import mynt.util.BufferUtil;
import mynt.util.NetworkUtil;
import static myntlogger.Logger.getLogger;

public final class PacketLoginStart implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        BufferUtil.readString(client, username -> {
            client.setProfileName(username);

            getLogger().info(username + " is attempting to log in!");

            byte[] token = new byte[4];

            NetworkUtil.getSecureRandom().nextBytes(token);

            client.setVerificationToken(token);

            getLogger().info("Verification Token (Before Encoding): " + Arrays.toString(token));

            new PacketEncryptionRequest(token).writeAndFlush(client);
        });
    }

}
