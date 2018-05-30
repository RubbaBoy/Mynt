package mynt.util;

public final class EntityUtil {

    private static int id;

    private EntityUtil() {

    }

    public static int getNextId() {
        return id++;
    }

}
