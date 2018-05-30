package mynt.entity;

import mynt.attribute.Location;
import mynt.util.EntityUtil;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    private int id;

    private Location location;

    public Entity(Location location) {
        id = EntityUtil.getNextId();

        this.location = location;
    }

    public final int getId() {
        return id;
    }

    public final Location getLocation() {
        return location;
    }

}
