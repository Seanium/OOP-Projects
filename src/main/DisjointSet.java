package main;

import java.util.HashMap;

public class DisjointSet {
    private final HashMap<Integer, Integer> parent = new HashMap<>();
    private final HashMap<Integer, Integer> rank = new HashMap<>();

    public void add(int id) {
        parent.put(id, id);
        rank.put(id, 0);
    }

    public int find(int id) {
        int root = id;
        while (root != parent.get(root)) {
            root = parent.get(root);
        }
        int temp = id;
        while (temp != root) {
            parent.replace(temp, root);    //路径压缩
            temp = parent.get(temp);
        }
        return root;
    }

    public void merge(int id1, int id2) {
        int root1 = find(id1);
        int root2 = find(id2);
        int rank1 = rank.get(root1);
        int rank2 = rank.get(root2);
        if (rank1 < rank2) {
            parent.put(root1, root2);
        } else if (rank1 > rank2) {
            parent.put(root2, root1);
        } else {
            parent.put(root2, root1);
            rank.put(root1, rank1 + 1);
        }
    }

    public void setParent(int son, int par) {
        parent.put(son, par);
    }

    public int getParent(int son) {
        return parent.get(son);
    }
}
