public class Person {
    private final int id;
    private final int fromFloor;
    private final int toFloor;
    private final int direction;    // -1:down, 1:up

    public int getDirection() {
        return direction;
    }

    public int getId() {
        return id;
    }

    public int getToFloor() {
        return toFloor;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public Person(int id, int fromFloor, int toFloor) {
        this.id = id;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.direction = (toFloor > fromFloor) ? 1 : -1;
    }
}
