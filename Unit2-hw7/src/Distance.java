import java.util.ArrayList;

public class Distance {
    private final int[][] dis = new int[12][12];
    private final int nodeCnt;

    public Distance(int nodeCnt) {
        this.nodeCnt = nodeCnt;
        reset();
    }

    public void floyd() {
        for (int k = 1; k <= nodeCnt; k++) {
            for (int i = 1; i <= nodeCnt; i++) {
                for (int j = 1; j <= nodeCnt; j++) {
                    dis[i][j] = Math.min(dis[i][j], dis[i][k] + dis[k][j]);
                }
            }
        }
    }

    public void addEdge(int point1, int point2, int length) {
        dis[point1][point2] = length;
        dis[point2][point1] = length;
    }

    public int check(int point1, int point2) {
        return dis[point1][point2];
    }

    public void addConnectedPoints(ArrayList<Boolean> accessArray) {
        for (int i = 1; i < nodeCnt; i++) {
            if (accessArray.get(i)) {
                for (int j = i + 1; j <= nodeCnt; j++) {
                    if (accessArray.get(j)) {
                        addEdge(i, j, Math.abs(i - j));
                    }
                }
            }
        }
    }

    public void reset() {
        for (int i = 1; i <= nodeCnt; i++) {
            for (int j = 1; j <= nodeCnt; j++) {
                if (i == j) {
                    dis[i][j] = 0;
                } else {
                    int inf = (int) 1e9;
                    dis[i][j] = inf;
                }
            }
        }
    }

    public void update(ArrayList<ArrayList<Boolean>> accessArrays) {
        reset();
        for (ArrayList<Boolean> arrayList : accessArrays) {
            addConnectedPoints(arrayList);
        }
        floyd();
        //print();
    }

    public void print() {
        for (int i = 1; i <= nodeCnt; i++) {
            for (int j = 1; j <= nodeCnt; j++) {
                int dis = check(i, j);
                if (dis == 1e9) {
                    System.out.print(i + "->" + j + ":\t" + " " + "\t");
                } else {
                    System.out.print(i + "->" + j + ":\t" + dis + "\t");
                }
            }
            System.out.println();
        }
        System.out.println("###################");
    }
}
