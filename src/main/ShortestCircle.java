package main;

import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;

public class ShortestCircle {
    private static HashMap<Integer, Integer> dis = new HashMap<>();
    private static HashSet<Integer> vis = new HashSet<>();
    private static PriorityQueue<Node> heap = new PriorityQueue<>();
    private static HashSet<Edge> edges = new HashSet<>();
    private static HashMap<Integer, Integer> branches = new HashMap<>();
    private static HashMap<Integer, Integer> preIds = new HashMap<>();

    public static int findShortestCircle(HashMap<Integer, Person> people, int id) {
        dij(people, id);
        int minDis = Integer.MAX_VALUE;
        for (Edge edge : edges) {
            int id1 = edge.getId1();
            int id2 = edge.getId2();
            if (!Objects.equals(branches.get(id1), branches.get(id2))) {
                int curDis = ((MyPerson) people.get(id1)).getValue().get(id2)
                        + dis.get(id1) + dis.get(id2);
                minDis = Math.min(curDis, minDis);
            }
        }
        return minDis == Integer.MAX_VALUE ? -1 : minDis;
    }

    private static void dij(HashMap<Integer, Person> people, int fromId) {
        dis.clear();
        vis.clear();
        heap.clear();
        edges.clear();
        branches.clear();
        preIds.clear();
        for (Integer i : people.keySet()) {
            dis.put(i, Integer.MAX_VALUE);
        }
        dis.put(fromId, 0);
        heap.add(new Node(fromId, 0));
        int branchId = 0;
        branches.put(fromId, branchId++);
        while (!heap.isEmpty()) {
            //加入
            Node curNode = heap.poll();
            int curId = curNode.getId();
            if (vis.contains(curId)) {
                continue;
            }
            vis.add(curId);
            if (!branches.containsKey(curId)) {
                int preId = preIds.get(curId);
                if (branches.get(preId) == 0) {
                    branches.put(curId, branchId++);
                } else {
                    branches.put(curId, branches.get(preId));
                }
                edges.remove(new Edge(curId, preId));
            }
            //更新
            MyPerson person = (MyPerson) people.get(curId);
            HashMap<Integer, Person> acquaintance = person.getAcquaintance();
            for (Integer friendId : acquaintance.keySet()) {
                int friendDis = person.queryValue(people.get(friendId));
                if (!vis.contains(friendId)) {
                    edges.add(new Edge(curId, friendId));
                    if (dis.get(curId) + friendDis < dis.get(friendId)) {
                        dis.put(friendId, dis.get(curId) + friendDis);
                        heap.add(new Node(friendId, dis.get(friendId)));
                        preIds.put(friendId, curId);
                    }
                }
            }
        }
    }
}
