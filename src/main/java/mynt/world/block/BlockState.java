package mynt.world.block;

import java.io.Serializable;
import java.util.Objects;

public final class BlockState implements Serializable {

    private final int id;

    private final int data;

    public BlockState(int id, int data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlockState)) {
            return false;
        }

        BlockState state = (BlockState) o;

        return id == state.id && data == state.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }

    @Override
    public String toString() {
        return "BlockState [ID: " + id + ", Data: " + data + "]";
    }

}
