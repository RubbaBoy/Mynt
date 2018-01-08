package mynt.network.packet.outgoing.login;

import mynt.network.packet.incoming.login.LoginResponse;
import myntnet.packet.outgoing.PacketBuilder;
import myntnet.packet.outgoing.PacketOutgoing;

import java.util.regex.Pattern;

public final class PacketLoginSuccess implements PacketOutgoing {

    private static final Pattern PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    private final String id;

    private final String name;

    public PacketLoginSuccess(LoginResponse response) {
        this.id = PATTERN.matcher(response.getId()).replaceAll("$1-$2-$3-$4-$5");
        this.name = response.getName();
    }

    @Override
    public void write(PacketBuilder builder) {
        builder.putString(id);
        builder.putString(name);
    }

    @Override
    public int getOpcode() {
        return 2;
    }

}
