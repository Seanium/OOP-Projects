import com.oocourse.elevator2.PersonRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Table {
    private final int bottomFloor;
    private final int topFloor;
    private final HashMap<Integer, Queue<Person>> waiters = new HashMap<>(); // 出发楼层：乘客队列
    private boolean end;
    private final HashMap<Integer, Boolean> maintainableMap = new HashMap<>();  // 记录维护请求
    private int acceptingCnt;

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

    public Table(int bottomFloor, int topFloor) {
        this.end = false;
        this.bottomFloor = bottomFloor;
        this.topFloor = topFloor;
        for (int i = bottomFloor; i <= topFloor; i++) {
            Queue<Person> personQueue = new LinkedList<>();
            this.waiters.put(i, personQueue);
        }
        this.acceptingCnt = 0;
    }

    public synchronized boolean hasWaiter() {   //是否有等待者(电梯外)
        for (int i = bottomFloor; i <= topFloor; i++) {
            if (!this.waiters.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean hasWaiterForward(int direction, int curFloor) {  //前进区间上有等待者
        int forwardBottomFloor = direction == -1 ? bottomFloor : curFloor + 1;  //前进区间的左端点
        int forwardTopFloor = direction == -1 ? curFloor - 1 : topFloor;        //前进区间的右端点
        for (int i = forwardBottomFloor; i <= forwardTopFloor; i++) {
            if (!waiters.get(i).isEmpty()) {
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

    public synchronized Person take(int floor, int direction) {
        Queue<Person> personQueue = waiters.get(floor);
        for (Person person : personQueue) {
            if (person.getDirection() == direction) {
                personQueue.remove(person);
                notifyAll();
                return person;
            }
        }
        return null;
    }

    public synchronized void waitForNew() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
