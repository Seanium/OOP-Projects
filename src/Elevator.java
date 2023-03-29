import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Elevator extends Thread {
    private final Table table;
    private final int id;
    private int curFloor;
    private int direction;  // -1:down, 1:up
    private final HashMap<Integer, Queue<Person>> passengers = new HashMap<>();    // 目的楼层: 乘客集合
    private int passengerCnt;
    private final int capacity;
    private final double speed;
    private final int bottomFloor;
    private final int topFloor;

    public Elevator(Table table, int id, int curFloor, int capacity, double speed) {
        this.table = table;
        this.id = id;
        this.curFloor = curFloor;
        this.direction = 1;
        this.passengerCnt = 0;
        this.capacity = capacity;
        this.speed = speed;
        this.bottomFloor = 1;
        this.topFloor = 11;
        for (int i = bottomFloor; i <= topFloor; i++) {
            Queue<Person> personQueue = new LinkedList<>();
            this.passengers.put(i, personQueue);
        }
        table.setMaintainable(id, false);
    }

    @Override
    public void run() {
        while (true) {
            // 开门，下客，上客，关门
            exchange();

            // 决策，然后反向或移动，然后到达
            if (decide() == 1) {
                return;
            }
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasPassenger() {   //是否有乘客(电梯内)
        return passengerCnt > 0;
    }

    public void openDoor() {
        TimableOutput.println("OPEN-" + curFloor + "-" + id);
        sleep(200);
    }

    public void closeDoor() {
        sleep(200);
        TimableOutput.println("CLOSE-" + curFloor + "-" + id);
    }

    public void exchange() {
        boolean out = !passengers.get(curFloor).isEmpty();
        int maxInCnt = capacity - (passengerCnt - passengers.get(curFloor).size());
        ArrayList<Person> personArrayList = new ArrayList<>();
        for (int i = 0; i < maxInCnt; i++) {
            Person person;
            if ((person = table.take(curFloor, direction)) == null) {
                break;
            } else {
                personArrayList.add(person);
            }
        }
        boolean in = personArrayList.size() > 0;
        if (in || out) {
            //开门
            openDoor();
            //下客
            if (out) {
                passengerCnt -= passengers.get(curFloor).size();
                for (Person person : passengers.get(curFloor)) {
                    TimableOutput.println("OUT-" + person.getId() + "-" + curFloor + "-" + id);
                }
                passengers.get(curFloor).clear();
            }
            //上客
            if (in) {
                for (Person person : personArrayList) {
                    TimableOutput.println("IN-" + person.getId() + "-" + curFloor + "-" + id);
                    passengers.get(person.getToFloor()).offer(person);
                    passengerCnt++;
                }
            }
            //关门
            closeDoor();
        }
    }

    // 需要结束线程则返回1
    public int decide() {
        if (table.isMaintainable(id)) {
            if (hasPassenger()) {
                openDoor();
                passengerCnt = 0;
                for (int i = bottomFloor; i <= topFloor; i++) {
                    for (Person person : passengers.get(i)) {
                        TimableOutput.println("OUT-" + person.getId() + "-" + curFloor + "-" + id);
                        table.put(new Person(person.getId(), curFloor, i));
                    }
                    passengers.get(i).clear();
                }
                closeDoor();
            }
            TimableOutput.println("MAINTAIN_ABLE-" + id);
            return 1;
        } else {
            if (table.hasWaiter() || this.hasPassenger()) {
                if (table.hasWaiterForward(direction, curFloor) || this.hasPassenger()) {
                    curFloor += direction;
                    sleep((int) (speed * 1000));
                    TimableOutput.println("ARRIVE-" + curFloor + "-" + id);
                } else {
                    direction = -direction;
                }
            } else {
                if (table.isEnd()) {
                    return 1;
                } else {
                    table.waitForNew();
                }
            }
        }
        return 0;
    }
}
