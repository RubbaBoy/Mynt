package mynt.world.block;

public enum Material {

    AIR(0),
    STONE(1),
    GRASS(2),
    DIRT(3);

    private int id;

    Material(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
