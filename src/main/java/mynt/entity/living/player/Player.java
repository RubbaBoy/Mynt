package mynt.entity.living.player;

import mynt.attribute.Location;
import mynt.entity.living.EntityLiving;

import java.io.Serializable;
import simplenet.Client;

public final class Player extends EntityLiving implements Serializable {

    private static final int MAX_HEALTH = 20;

    private final Client client;

    public Player(Client client) {
        super(MAX_HEALTH, new Location(0, 62, 0));
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

}
