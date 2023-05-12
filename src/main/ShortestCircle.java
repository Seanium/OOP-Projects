package main;

import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class ShortestCircle {
    private static HashMap<Integer, Integer> dis = new HashMap<>();
    private static HashSet<Integer> vis = new HashSet<>();
    private static PriorityQueue<Node> heap = new PriorityQueue<>();

    public static int findShortestCircle(HashMap<Integer, Person> people, int id) {
        MyPerson person = (MyPerson) people.get(id);
        HashMap<Integer, Person> acquaintance = person.getAcquaintance();
        HashMap<Integer, Integer> value = person.getValue();
        int minDis = Integer.MAX_VALUE;
        int failTime = 0;
        HashSet<Integer> friendIds = new HashSet<>(acquaintance.keySet());
        for (Integer friendId : friendIds) {
            MyPerson friend = (MyPerson) people.get(friendId);
            //删边
            acquaintance.remove(friendId, friend);
            final int friendDis = value.remove(friendId);
            friend.getAcquaintance().remove(id);
            friend.getValue().remove(id);
            //dij
            final int dijDis = dij(people, id, friendId);
            //把边加回
            acquaintance.put(friendId, friend);
            value.put(friendId, friendDis);
            friend.getAcquaintance().put(id, person);
            friend.getValue().put(id, friendDis);
            if (dijDis == -1) {
                failTime++;
                continue;
            }
            minDis = Math.min(dijDis + friendDis, minDis);
        }
        return failTime == acquaintance.size() ? -1 : minDis;
    }

    private static int dij(HashMap<Integer, Person> people, int fromId, int toId) {
        dis.clear();
        vis.clear();
        heap.clear();
        for (Integer i : people.keySet()) {
            dis.put(i, Integer.MAX_VALUE);
        }
        dis.put(fromId, 0);
        heap.add(new Node(fromId, 0));
        while (!heap.isEmpty()) {
            Node curNode = heap.poll();
            int curId = curNode.getId();
            if (vis.contains(curId)) {
                continue;
            }
            vis.add(curId);
            MyPerson person = (MyPerson) people.get(curId);
            HashMap<Integer, Person> acquaintance = person.getAcquaintance();
            for (Integer friendId : acquaintance.keySet()) {
                Person friend = people.get(friendId);
                int friendDis = person.queryValue(friend);
                if (!vis.contains(friendId) && dis.get(curId) + friendDis < dis.get(friendId)) {
                    dis.put(friendId, dis.get(curId) + friendDis);
                    heap.add(new Node(friendId, dis.get(friendId)));
                }
            }
        }
        return dis.get(toId) == Integer.MAX_VALUE ? -1 : dis.get(toId);
    }
}
