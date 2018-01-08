package mynt.network.packet.incoming.login;

import mynt.network.packet.outgoing.login.PacketEncryptionRequest;
import myntnet.client.Client;
import myntnet.packet.incoming.impl.PacketReadable;
import myntnet.packet.outgoing.PacketOutgoing;
import myntnet.util.BufferUtil;
import mynt.util.NetworkUtil;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static myntlogger.Logger.getLogger;

public final class PacketLoginStart implements PacketReadable {

    @Override
    public void read(Client client, ByteBuffer buffer) {
        String username = BufferUtil.getString(buffer);

        client.setProfileName(username);

        getLogger().info(username + " is attempting to log in!");

        byte[] token = new byte[4];

        NetworkUtil.getSecureRandom().nextBytes(token);

        client.setVerificationToken(token);

        getLogger().info("Verification Token (Before Encoding): " + Arrays.toString(token));

        client.write(new PacketEncryptionRequest(token));
    }

}
