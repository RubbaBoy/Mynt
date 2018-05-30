package mynt.network.packet.outgoing.impl.login;

import mynt.network.packet.outgoing.PacketOutgoing;
import mynt.util.BufferUtil;
import mynt.util.NetworkUtil;
import simplenet.packet.Packet;

public final class PacketEncryptionRequest implements PacketOutgoing {

    private final byte[] encodedPublicKey = NetworkUtil.getEncodedPublicKey();

    private final byte[] verificationToken;

    public PacketEncryptionRequest(byte[] verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Override
    public void write(Packet packet) {
        BufferUtil.putString("", packet);
        BufferUtil.putVarInt(encodedPublicKey.length, packet);
        packet.putBytes(encodedPublicKey);
        BufferUtil.putVarInt(verificationToken.length, packet);
        packet.putBytes(verificationToken);
    }

    @Override
    public int getOpcode() {
        return 1;
    }

}
