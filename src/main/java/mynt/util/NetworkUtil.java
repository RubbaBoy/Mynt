package mynt.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public final class NetworkUtil {

    private static final Cipher DECRYPT_CIPHER;

    private static final byte[] ENCODED_PUBLIC_KEY;

    private static final KeyFactory KEY_FACTORY;

    private static final KeyPairGenerator KEY_PAIR_GENERATOR;

    private static final X509EncodedKeySpec KEY_SPEC;

    private static final PublicKey PUBLIC_KEY;

    private static final PrivateKey PRIVATE_KEY;

    private static final SecureRandom RANDOM;

    private static final MessageDigest SHA;

    static {
        try {
            KEY_FACTORY = KeyFactory.getInstance("RSA");
            KEY_PAIR_GENERATOR = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to generate RSA key pair!");
        }

        KEY_PAIR_GENERATOR.initialize(1024);

        KeyPair keyPair = KEY_PAIR_GENERATOR.genKeyPair();

        PUBLIC_KEY = keyPair.getPublic();
        PRIVATE_KEY = keyPair.getPrivate();

        try {
            KEY_SPEC = KEY_FACTORY.getKeySpec(PUBLIC_KEY, X509EncodedKeySpec.class);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Unable to get the DER key spec!");
        }

        ENCODED_PUBLIC_KEY = KEY_SPEC.getEncoded();

        try {
            RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to get SecureRandom instance!");
        }

        try {
            DECRYPT_CIPHER = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize the decrypt cipher!");
        }

        try {
            SHA = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize the SHA-1 message digest!");
        }
    }

    private NetworkUtil() {

    }

    public static byte[] decrypt(byte[] b) {
        try {
            return DECRYPT_CIPHER.doFinal(b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getEncodedPublicKey() {
        return ENCODED_PUBLIC_KEY;
    }

    public static SecureRandom getSecureRandom() {
        return RANDOM;
    }

    public static MessageDigest getSHA() {
        return SHA;
    }

}
