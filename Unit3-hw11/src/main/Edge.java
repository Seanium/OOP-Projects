package main;

import java.util.Objects;

public class Edge {
    private final Integer id1;
    private final Integer id2;

    public Edge(Integer id1, Integer id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
    }

    public Integer getId1() {
        return id1;
    }

    public Integer getId2() {
        return id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return Objects.equals(id1, edge.id1) && Objects.equals(id2, edge.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }
}
