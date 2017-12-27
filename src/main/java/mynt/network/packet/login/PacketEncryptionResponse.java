package mynt.network.packet.login;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import myntnet.client.Client;
import myntnet.packet.incoming.impl.PacketReadable;
import myntnet.util.BufferUtil;
import util.NetworkUtil;

import java.math.BigInteger;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;

import static myntlogger.Logger.getLogger;

public final class PacketEncryptionResponse implements PacketReadable {

    @Override
    public void read(Client client, ByteBuffer buffer) {
        byte[] encodedSecret = new byte[BufferUtil.getVarInt(buffer)];

        buffer.get(encodedSecret);

        byte[] encodedToken = new byte[BufferUtil.getVarInt(buffer)];

        buffer.get(encodedToken);

        byte[] decodedToken = NetworkUtil.decrypt(encodedToken);

        if (decodedToken == null) {
            //TODO: Send 0x00 kick packet.
            return;
        }

        if (!Arrays.equals(decodedToken, client.getVerificationToken())) {
            //TODO: Send 0x00 kick packet.
            return;
        }

        byte[] decodedSecret = NetworkUtil.decrypt(encodedSecret);

        /*
         * The decoded secret should never be null
         * if the verification token is valid, but
         * return just in case.
         */
        if (decodedSecret == null) {
            return;
        }

        client.setSharedSecret(decodedSecret);

        getLogger().info("Verification Token (After Decoding): " + Arrays.toString(decodedToken));

        MessageDigest SHA = NetworkUtil.getSHA();

        SHA.reset();
        SHA.update(decodedSecret);
        SHA.update(NetworkUtil.getEncodedPublicKey());

        byte[] hashBytes = SHA.digest();

        String hash = new BigInteger(1, hashBytes).toString(16);

        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + client.getProfileName() + "&serverId=" + hash))
                    .GET()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandler.asString())
                      .thenAccept(response -> getLogger().info(response.body()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
