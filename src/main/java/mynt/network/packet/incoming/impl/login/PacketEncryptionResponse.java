package mynt.network.packet.incoming.impl.login;

import java.util.concurrent.TimeUnit;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import mynt.MinecraftServer;
import mynt.network.MyntClient;
import mynt.network.State;
import mynt.network.packet.incoming.PacketIncoming;
import mynt.network.packet.outgoing.impl.login.PacketLoginSuccess;
import mynt.network.packet.outgoing.impl.play.PacketJoinGame;
import mynt.network.packet.outgoing.impl.play.PacketKeepAlive;
import mynt.network.packet.outgoing.impl.play.PacketPlayerAbilities;
import mynt.network.packet.outgoing.impl.play.PacketSpawnPosition;
import mynt.util.BufferUtil;
import mynt.util.NetworkUtil;

import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.util.Arrays;

import static myntlogger.Logger.getLogger;
import static mynt.util.GsonUtil.GSON;

public final class PacketEncryptionResponse implements PacketIncoming {

    @Override
    public void read(MyntClient client) {
        BufferUtil.readVarInt(client, encodedSecretLength -> {
            client.read(encodedSecretLength, encodedSecretPayload -> {
                byte[] encodedSecret = new byte[encodedSecretLength];

                encodedSecretPayload.get(encodedSecret);

                BufferUtil.readVarInt(client, encodedTokenLength -> {
                    client.read(encodedTokenLength, encodedTokenPayload -> {
                        byte[] encodedToken = new byte[encodedTokenLength];

                        encodedTokenPayload.get(encodedToken);

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
                                        MinecraftServer.addPlayer(client);

                                        client.setState(State.PLAY);

                                        LoginResponse loginResponse = GSON.fromJson(response.body(), LoginResponse.class);

                                        new PacketLoginSuccess(loginResponse).write(client);
                                        new PacketJoinGame().write(client);
                                        new PacketSpawnPosition().write(client);
                                        new PacketPlayerAbilities().writeAndFlush(client);

                                        MinecraftServer.getExecutor().scheduleAtFixedRate(() -> {
                                            new PacketKeepAlive(client).writeAndFlush(client);
                                        }, 0, 5, TimeUnit.SECONDS);
                                    }).exceptionally(t -> {
                                        t.printStackTrace();
                                        return null;
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            });
        });
    }

}
