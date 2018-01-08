package mynt.network.packet.outgoing.login;

import mynt.util.NetworkUtil;
import myntnet.packet.outgoing.PacketBuilder;
import myntnet.packet.outgoing.PacketOutgoing;

public final class PacketEncryptionRequest implements PacketOutgoing {

    private final byte[] encodedPublicKey = NetworkUtil.getEncodedPublicKey();

    private final byte[] verificationToken;

    public PacketEncryptionRequest(byte[] verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Override
    public void write(PacketBuilder builder) {
        builder.putString("");
        builder.putVarInt(encodedPublicKey.length);
        builder.putBytes(encodedPublicKey);
        builder.putVarInt(verificationToken.length);
        builder.putBytes(verificationToken);
    }

    @Override
    public int getOpcode() {
        return 1;
    }

}
