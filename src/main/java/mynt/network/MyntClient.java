package mynt.network;

import java.util.ArrayDeque;
import java.util.Queue;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import simplenet.Client;
import simplenet.Server;

public final class MyntClient extends Client {

    private final Client client;

    private final Queue<Long> keepAlivePackets;

    private int keepAlivePacketsReceived;

    /**
     * The name of the profile used for logging on to a {@link Server}.
     * <p>
     * This is NOT to be used as the username of this {@link Client}.
     */
    private String profileName;

    private State state = State.HANDSHAKING;

    /**
     * This {@link Client}'s verification token for the
     * game client and {@link Server}.
     */
    private byte[] verificationToken;

    public MyntClient(Client client) {
        super(client);

        this.client = client;
        this.keepAlivePackets = new ArrayDeque<>();
    }

    /**
     * Sets this {@link Client}'s shared secret.
     *
     * @param sharedSecret
     *      The new shared secret.
     */
    public void setSharedSecret(SecretKey sharedSecret) {
        try {
            Cipher AES_ENCRYPT_CIPHER = Cipher.getInstance("AES/CFB8/NoPadding");
            AES_ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, sharedSecret, new IvParameterSpec(sharedSecret.getEncoded()));

            Cipher AES_DECRYPT_CIPHER = Cipher.getInstance("AES/CFB8/NoPadding");
            AES_DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, sharedSecret, new IvParameterSpec(sharedSecret.getEncoded()));

            setEncryption(AES_ENCRYPT_CIPHER);
            setDecryption(AES_DECRYPT_CIPHER);

            client.setDecryption(AES_DECRYPT_CIPHER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets this {@link Client}'s profile name.
     *
     * @return
     *      A {@link String}.
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Sets this {@link Client}'s profile name.
     *
     * @param profileName
     *      The new profile name.
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * Gets this {@link Client}'s verification token.
     *
     * @return
     *      A {@code byte[]}.
     */
    public byte[] getVerificationToken() {
        return verificationToken;
    }

    /**
     * Sets this {@link Client}'s verification token.
     *
     * @param verificationToken
     *      The new verificationToken.
     */
    public void setVerificationToken(byte[] verificationToken) {
        this.verificationToken = verificationToken;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Queue<Long> getKeepAlivePackets() {
        return keepAlivePackets;
    }

    public int getKeepAlivePacketsReceived() {
        return keepAlivePacketsReceived;
    }

    public void incrementKeepAlivePacketsReceived() {
        keepAlivePacketsReceived++;
    }

    public void resetKeepAlivePacketsReceived() {
        keepAlivePacketsReceived = 0;
    }

}
