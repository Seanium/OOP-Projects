package main;

public class Node implements Comparable<Node> {
    private final int id;
    private final int dis;

    public Node(int id, int dis) {
        this.id = id;
        this.dis = dis;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.dis, o.dis);
    }

    public int getId() {
        return id;
    }

}
