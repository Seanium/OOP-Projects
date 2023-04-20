package main;

import java.util.HashMap;

public class DisjointSet {
    private final HashMap<Integer, Integer> parent = new HashMap<>();

    public void add(int id) {
        parent.put(id, id);
    }

    public int find(int id) {
        int root = id;
        while (root != parent.get(root)) {
            root = parent.get(root);
        }
        int temp = id;
        while (temp != parent.get(temp)) {
            temp = parent.get(temp);
            parent.replace(temp, root);    //路径压缩
        }
        return root;
    }

    public void merge(int id1, int id2) {
        parent.put(find(id1), find(id2));
    }
}
