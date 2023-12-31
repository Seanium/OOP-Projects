import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Elevator extends Thread {
    private final Table table;
    private final int id;
    private int curFloor;
    private int direction;  // -1:down, 1:up
    private final Queue<Person> passengers = new LinkedList<>();
    private final int capacity;
    private final double speed;
    private final int access;
    private final ArrayList<Boolean> accessArray;   //可达楼层数组 下标从1开始

    public Elevator(Table table, int id, int curFloor, int capacity, double speed, int access) {
        this.table = table;
        this.id = id;
        this.curFloor = curFloor;
        this.direction = 1;
        this.capacity = capacity;
        this.speed = speed;
        table.setMaintainable(id, false);
        this.access = access;
        this.accessArray = new ArrayList<>();
        accessArray.add(false);
        for (int i = 1; i <= 11; i++) {
            accessArray.add(canAccess(i) ? Boolean.TRUE : Boolean.FALSE);
        }
        table.addAccessArray(accessArray);
        table.updateDistance();
    }

    @Override
    public void run() {
        while (true) {
            //System.out.println("电梯 " + id + " 的轮询 start");
            //如果收到维护请求
            if (table.isMaintainable(id)) {
                if (maintain() == 1) {
                    return;
                }
            } else {  //如果没有维护请求
                //如果本层可达
                if (canAccess(curFloor)) {
                    //是否需要下客
                    ArrayList<Person> outArr = new ArrayList<>();
                    if (passengers.size() > 0) {
                        Iterator<Person> iterator = passengers.iterator();
                        while (iterator.hasNext()) {
                            Person person = iterator.next();
                            if (person.getEleDest() == curFloor) {
                                outArr.add(person);
                                iterator.remove();
                            }
                        }
                    }
                    boolean out = !outArr.isEmpty();
                    //是否需要上客
                    int maxInCnt = capacity - passengers.size();
                    ArrayList<Person> inArr = new ArrayList<>();
                    for (int i = 0; i < maxInCnt; i++) {
                        Person person;
                        if ((person = table.take(curFloor, accessArray)) == null) {
                            //System.out.println("Elevator " + id + " order person: null");
                            break;
                        } else {
                            //System.out.println("Elevator " + id
                            // + " order person: " + person.getId());
                            inArr.add(person);
                        }
                    }
                    boolean in = !inArr.isEmpty();
                    if (out || in) {
                        exchange(in, out, inArr, outArr);
                    }
                }
                if (decide() == 1) {
                    return;
                }
            }
        }
    }

    private int maintain() {
        if (passengers.size() > 0) {
            while (!table.newOtherServe(curFloor)) {
                //System.out.println("电梯 " + id + " 不能服务，进入等待");
                //System.out.println("电梯 " + id + " 的 wait 开始");
                table.waitForNew();
                //System.out.println("电梯 " + id + " 的 wait 结束");
            }
            openDoor();
            for (Person person : passengers) {
                TimableOutput.println("OUT-" + person.getId() + "-" + curFloor + "-" + id);
                if (curFloor != person.getToFloor()) {  //未到目的地，需要继续换乘的乘客
                    table.put(new Person(person.getId(), curFloor, person.getToFloor()));
                } else { //已到目的地的乘客
                    table.changeMovingCntBy(-1);
                }
            }
            //passengers.clear(); //此处可省略
            closeDoor();
            table.delOtherServe(curFloor);
        }
        TimableOutput.println("MAINTAIN_ABLE-" + id);
        //TimableOutput.println(id + " elevator end");
        table.removeAccessArray(accessArray);
        table.updateDistance();
        return 1;
    }

    private void exchange(boolean in, boolean out,
                         ArrayList<Person> inArr, ArrayList<Person> outArr) {
        //控制同层服务数量
        if (in && !out) {   //服务-只接人
            //System.out.println("电梯 " + id + " 想要只接人");
            while (!table.newOnlyIn(curFloor)) {
                //System.out.println("电梯 " + id + " 不能只接人，进入等待");
                //System.out.println("电梯 " + id + " 的 wait 开始");
                table.waitForNew();
                //System.out.println("电梯 " + id + " 的 wait 结束");
            }
            //System.out.println("电梯 " + id + " 开始服务（只接人）");
        } else {    //服务-非只接人
            //System.out.println("电梯 " + id + " 想要服务");
            while (!table.newOtherServe(curFloor)) {
                //System.out.println("电梯 " + id + " 不能服务，进入等待");
                //System.out.println("电梯 " + id + " 的 wait 开始");
                table.waitForNew();
                //System.out.println("电梯 " + id + " 的 wait 结束");
            }
            //System.out.println("电梯 " + id + " 开始服务");
        }
        //开门
        openDoor();
        //下客
        if (out) {
            for (Person person : outArr) {
                TimableOutput.println("OUT-" + person.getId() +
                        "-" + curFloor + "-" + id);
                if (curFloor != person.getToFloor()) {  //未到目的地，需要继续换乘的乘客
                    table.put(new Person(person.getId(),
                            curFloor, person.getToFloor()));
                //System.out.println("乘客 " + person.getId()
                // + " 回到第 " + curFloor + " 层等待队列");
                } else { //已到目的地的乘客
                    table.changeMovingCntBy(-1);
                }
            }
        }
        //上客
        if (in) {
            for (Person person : inArr) {
                TimableOutput.println("IN-" + person.getId() +
                        "-" + curFloor + "-" + id);
                passengers.offer(person);
            }
        }
        //关门
        closeDoor();
        if (in && !out) {   //服务-只接人
            //System.out.println("电梯 " + id + " 结束服务（只接人）");
            table.delOnlyIn(curFloor);
        } else {   //服务-非只接人
            //System.out.println("电梯 " + id + " 结束服务");
            table.delOtherServe(curFloor);
        }
    }

    private int decide() {
        //如果有乘客
        if (passengers.size() > 0) {
            direction = (passengers.element().getEleDest() > curFloor) ? 1 : -1;
            moveForward();
        } else {  //如果没有乘客
            //如果有等待者在可达层
            if (table.hasWaiter(accessArray)) {
                //如果有等待者在前进方向可达层
                if (table.hasWaiterForward(direction, curFloor, accessArray)) {
                    moveForward();
                } else if (table.hasWaiterBackward(direction, curFloor, accessArray)) {
                    direction = -direction;
                } else {
                    table.waitForNew();
                }
            } else {  //如果没有等待者在可达层
                //如果输入已结束并且接收数==0
                if (table.isEnd() && table.noMoving()) {
                    //TimableOutput.println(id + " 电梯线程结束");
                    table.removeAccessArray(accessArray);
                    table.updateDistance();
                    return 1;
                } else {
                    //System.out.println("电梯 " + id + " 的 wait 开始");
                    table.waitForNew();
                    //System.out.println("电梯 " + id + " 的 wait 结束");
                }
            }
        }
        return 0;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void openDoor() {
        TimableOutput.println("OPEN-" + curFloor + "-" + id);
        sleep(200);
    }

    public void closeDoor() {
        sleep(200);
        TimableOutput.println("CLOSE-" + curFloor + "-" + id);
    }

    public void moveForward() {
        curFloor += direction;
        sleep((int) (speed * 1000));
        TimableOutput.println("ARRIVE-" + curFloor + "-" + id);
    }

    private Boolean canAccess(int floor) {
        return (access & (1 << (floor - 1))) != 0;
    }
}
