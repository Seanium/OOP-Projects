import com.oocourse.elevator1.TimableOutput;

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

    public Elevator(Table table, int id) {
        this.table = table;
        this.id = id;
        this.curFloor = 1;
        this.direction = 1;
        int bottomFloor = 1;
        int topFloor = 11;
        this.passengerCnt = 0;
        this.capacity = 6;
        for (int i = bottomFloor; i <= topFloor; i++) {
            Queue<Person> personQueue = new LinkedList<>();
            this.passengers.put(i, personQueue);
        }
    }

    @Override
    public void run() {
        while (true) {
            // 开门，下客，上客，关门
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
                TimableOutput.println("OPEN-" + curFloor + "-" + id);
                sleep(200);
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
                sleep(200);
                TimableOutput.println("CLOSE-" + curFloor + "-" + id);
            }

            // 决策，然后反向或移动，然后到达
            if (table.hasWaiter() || this.hasPassenger()) {
                if (table.hasWaiterForward(direction, curFloor) || this.hasPassenger()) {
                    curFloor += direction;
                    sleep(400);
                    TimableOutput.println("ARRIVE-" + curFloor + "-" + id);
                } else {
                    direction = -direction;
                }
            } else {
                if (table.isEnd()) {
                    return;
                } else {
                    table.waitForNew();
                }
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
}
