import com.oocourse.elevator1.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();  // 初始化时间戳
        Table table = new Table(1, 11);
        new Input(table).start();
        for (int i = 1; i <= 6; i++) {
            new Elevator(table, i).start();
        }
    }
}
