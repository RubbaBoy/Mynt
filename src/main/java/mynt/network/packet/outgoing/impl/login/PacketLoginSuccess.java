package mynt.network.packet.outgoing.impl.login;

import mynt.network.packet.incoming.impl.login.LoginResponse;
import mynt.network.packet.outgoing.PacketOutgoing;
import mynt.util.BufferUtil;

import java.util.regex.Pattern;
import simplenet.packet.Packet;

public final class PacketLoginSuccess implements PacketOutgoing {

    private static final Pattern PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    private final String id;

    private final String name;

    public PacketLoginSuccess(LoginResponse response) {
        this.id = PATTERN.matcher(response.getId()).replaceAll("$1-$2-$3-$4-$5");
        this.name = response.getName();
    }

    @Override
    public void write(Packet packet) {
        BufferUtil.putString(id, packet);
        BufferUtil.putString(name, packet);
    }

    @Override
    public int getOpcode() {
        return 2;
    }

}
