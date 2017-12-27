package mynt.network.packet.status;

import java.util.concurrent.ThreadLocalRandom;

public final class StatusResponse {

    Version version = new Version("1.12.2", 340);

    Players players = new Players(420, ThreadLocalRandom.current().nextInt(0, 421));

    Chat description = new Chat("Hello World!");

}

class Version {

    private String name;

    private int protocol;

    public Version(String name, int protocol) {
        this.name = name;
        this.protocol = protocol;
    }

}

class Players {

    private int max;

    private int online;

    public Players(int max, int online) {
        this.max = max;
        this.online = online;
    }

}

class Chat {

    private String text;

    public Chat(String text) {
        this.text = text;
    }

}