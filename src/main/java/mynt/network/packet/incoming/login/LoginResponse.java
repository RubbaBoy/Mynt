package mynt.network.packet.incoming.login;

public final class LoginResponse {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

class Properties {

    private String name;

    private String value;

    private String signature;

}
