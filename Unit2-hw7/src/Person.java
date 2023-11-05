public class Person {
    private final int id;
    private final int fromFloor;
    private final int toFloor;
    private int eleDest;    // 电梯转运目的地

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
        this.eleDest = 0;
    }

    public void setEleDest(int eleDest) {
        this.eleDest = eleDest;
    }

    public int getEleDest() {
        return eleDest;
    }
}
