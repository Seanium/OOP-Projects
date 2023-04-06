import com.oocourse.elevator3.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();  // 初始化时间戳
        Table table = new Table(1, 11);
        new Input(table).start();
        for (int i = 1; i <= 6; i++) {
            new Elevator(table, i, 1, 6, 0.4, 2047).start();
        }
    }
}
