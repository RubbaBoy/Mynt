package mynt.entity.living;

import mynt.attribute.Location;
import mynt.entity.Entity;

import java.io.Serializable;

public abstract class EntityLiving extends Entity implements Serializable {

    private int health;

    private float pitch;

    private float yaw;

    public EntityLiving(int health, Location location) {
        super(location);

        this.health = health;
    }

    public final void incrementHealth(int health) {
        this.health += health;
    }

    public final void decrementHealth(int health) {
        this.health = Math.max(this.health - health, 0);
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(int health) {
        this.health = health;
    }

    public final float getPitch() {
        return pitch;
    }

    public final void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public final float getYaw() {
        return yaw;
    }

    public final void setYaw(float yaw) {
        this.yaw = yaw;
    }

}
