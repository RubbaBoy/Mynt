package mynt.network.packet.incoming.login;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import mynt.network.packet.outgoing.login.PacketLoginSuccess;
import mynt.network.packet.outgoing.play.PacketJoinGame;
import mynt.network.packet.outgoing.play.PacketPlayerAbilities;
import mynt.network.packet.outgoing.play.PacketSpawnPosition;
import myntnet.client.Client;
import myntnet.client.State;
import myntnet.packet.incoming.impl.PacketReadable;
import myntnet.packet.outgoing.PacketOutgoing;
import myntnet.util.BufferUtil;
import mynt.util.NetworkUtil;

import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Pattern;

import static myntlogger.Logger.getLogger;
import static mynt.util.GsonUtil.GSON;

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

        client.setSharedSecret(new SecretKeySpec(decodedSecret, "AES"));

        getLogger().info("Verification Token (After Decoding): " + Arrays.toString(decodedToken));

        MessageDigest SHA = NetworkUtil.getSHA();

        SHA.reset();
        SHA.update(decodedSecret);
        SHA.update(NetworkUtil.getEncodedPublicKey());

        String hash = new BigInteger(SHA.digest()).toString(16);

        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .uri(new URI("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + client.getProfileName() + "&serverId=" + hash))
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .GET().build();

            httpClient.sendAsync(request, HttpResponse.BodyHandler.asString())
                      .thenAccept(response -> {
                          client.setState(State.PLAY);

                          LoginResponse loginResponse = GSON.fromJson(response.body(), LoginResponse.class);

                          client.write(new PacketLoginSuccess(loginResponse));
                          client.write(new PacketJoinGame());
                          client.write(new PacketSpawnPosition());
                          client.write(new PacketPlayerAbilities());
                      }).exceptionally(throwable -> {
                         throwable.printStackTrace();
                         return null;
                      });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
