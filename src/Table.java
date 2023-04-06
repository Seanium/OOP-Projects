import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Table {
    private final int bottomFloor;
    private final int topFloor;
    private final HashMap<Integer, Queue<Person>> waiters = new HashMap<>(); // 出发楼层：乘客队列
    private boolean end;
    private final HashMap<Integer, Boolean> maintainableMap = new HashMap<>();  // 记录维护请求
    private int acceptingCnt;
    private final ArrayList<ArrayList<Boolean>> accessArrays = new ArrayList<>();
    private final Distance distance;

    public Table(int bottomFloor, int topFloor) {
        this.end = false;
        this.bottomFloor = bottomFloor;
        this.topFloor = topFloor;
        for (int i = bottomFloor; i <= topFloor; i++) {
            Queue<Person> personQueue = new LinkedList<>();
            this.waiters.put(i, personQueue);
        }
        this.acceptingCnt = 0;
        this.distance = new Distance(11);
    }

    public synchronized void changeAcceptingCntBy(int n) {
        acceptingCnt += n;
        notifyAll();
    }

    public synchronized boolean noAccepting() {
        return acceptingCnt == 0;
    }

    public synchronized void setMaintainable(int id, boolean maintainable) {
        maintainableMap.put(id, maintainable);
        notifyAll();
    }

    public synchronized boolean isMaintainable(int id) {
        return maintainableMap.get(id);
    }

    public synchronized void setEnd(boolean end) {
        this.end = end;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return end;
    }

    public synchronized void addAccessArray(ArrayList<Boolean> accessArray) {
        accessArrays.add(accessArray);
    }

    public synchronized void removeAccessArray(ArrayList<Boolean> accessArray) {
        accessArrays.remove(accessArray);
    }

    public synchronized void updateDistance() {
        distance.update(accessArrays);
    }

    public synchronized boolean hasWaiter(ArrayList<Boolean> accessArray) {   //是否有等待者(电梯外)
        for (int i = bottomFloor; i <= topFloor; i++) {
            if (accessArray.get(i) && !this.waiters.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    //前进区间上有等待者
    public synchronized boolean hasWaiterForward(int direction, int curFloor,
                                                 ArrayList<Boolean> accessArray) {
        int forwardBottomFloor = direction == -1 ? bottomFloor : curFloor + 1;  //前进区间的左端点
        int forwardTopFloor = direction == -1 ? curFloor - 1 : topFloor;        //前进区间的右端点
        for (int i = forwardBottomFloor; i <= forwardTopFloor; i++) {
            if (accessArray.get(i) && !waiters.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    //后退区间上有等待者
    public synchronized boolean hasWaiterBackward(int direction, int curFloor,
                                                  ArrayList<Boolean> accessArray) {
        int backwardBottomFloor = direction == 1 ? bottomFloor : curFloor + 1;  //后退区间的左端点
        int backwardTopFloor = direction == 1 ? curFloor - 1 : topFloor;        //后退区间的右端点
        for (int i = backwardBottomFloor; i <= backwardTopFloor; i++) {
            if (accessArray.get(i) && !waiters.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public synchronized void put(PersonRequest request) {
        Person person =
                new Person(request.getPersonId(), request.getFromFloor(), request.getToFloor());
        this.waiters.get(request.getFromFloor()).offer(person);
        notifyAll();
    }

    public synchronized void put(Person person) {
        this.waiters.get(person.getFromFloor()).offer(person);
        notifyAll();
    }

    public synchronized Person take(int curFloor, ArrayList<Boolean> accessArray) {
        Queue<Person> personQueue = waiters.get(curFloor);
        for (Person person : personQueue) {
            int eleDest = calEleDest(curFloor, person.getToFloor(), accessArray);
            if (eleDest != -1) {
                personQueue.remove(person);
                notifyAll();
                person.setEleDest(eleDest);
                return person;
            }
        }
        return null;
    }

    public synchronized ArrayList<Person> take(int curFloor,
                                               ArrayList<Boolean> accessArray, int maxInCnt) {
        ArrayList<Person> inArr = new ArrayList<>();
        Queue<Person> personQueue = waiters.get(curFloor);
        Iterator<Person> iterator = personQueue.iterator();
        while (iterator.hasNext()) {
            if (inArr.size() == maxInCnt) {
                break;
            }
            Person person = iterator.next();
            int eleDest = calEleDest(curFloor, person.getToFloor(), accessArray);
            if (eleDest != -1) {
                person.setEleDest(eleDest);
                inArr.add(person);
                iterator.remove();
            }
        }
        if (inArr.size() > 0) {
            notifyAll();
        }
        System.out.println(inArr.size());
        return inArr;
    }

    public synchronized int calEleDest(int curFloor, int toFloor, ArrayList<Boolean> accessArray) {
        int eleDest = -1;
        int dis = distance.check(curFloor, toFloor);
        for (int i = 1; i <= 11; i++) {
            if (accessArray.get(i) && i != curFloor) {
                int newDis = distance.check(i, toFloor);
                if (newDis < dis) {
                    eleDest = i;
                    dis = newDis;
                }
            }
        }
        return eleDest;
    }

    public synchronized void waitForNew() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
