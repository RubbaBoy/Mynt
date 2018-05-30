package mynt.attribute;

import java.io.Serializable;
import java.util.Objects;

public final class Location implements Serializable {

    private double x;

    private double y;

    private double z;

    public Location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static long encode(Location location) {
        return encode((int) location.getX(), (int) location.getY(), (int) location.getZ());
    }

    public static long encode(int x, int y, int z) {
        return ((x & 0x3FFFFFFL) << 38) | ((y & 0xFFFL) << 26) | (z & 0x3FFFFFFL);
    }

    public static Location decode(long l) {
        return new Location(l >> 38, (l >> 26) & 0xFFF, l << 38 >> 38);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location)) {
            return false;
        }

        Location location = (Location) o;

        return x == location.x && y == location.y && z == location.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Location [X: " + x + ", Y: " + y + ", Z: " + z + "]";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
