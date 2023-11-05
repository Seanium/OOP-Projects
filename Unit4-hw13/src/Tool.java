import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    private static ArrayList<String> commands = new ArrayList<>();
    private static int commandCnt;

    public static void getInput() {
        Scanner scanner = new Scanner(System.in);
        //初始化书
        int n = Integer.parseInt(scanner.nextLine());
        String regex1 = "(?<type>\\w)-(?<id>\\d+) (?<cnt>\\d+)";
        Pattern pattern1 = Pattern.compile(regex1);
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            Matcher matcher = pattern1.matcher(line);
            if (matcher.find()) {
                int cnt = Integer.parseInt(matcher.group("cnt"));
                for (int j = 0; j < cnt; j++) {
                    Shelf.add(new Book(matcher.group("type"), matcher.group("id")));
                }
            }
        }
        //读入操作
        commandCnt = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < commandCnt; i++) {
            commands.add(scanner.nextLine());
        }
    }

    public static void runCommands() {
        String regex2 = "(?<date>\\[(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)\\]) " +
                "(?<pid>\\d+) (?<op>\\w+) (?<type>\\w)-(?<id>\\d+)";
        Pattern pattern2 = Pattern.compile(regex2);
        String lastLine = commands.get(commandCnt - 1);
        Matcher lastMatcher = pattern2.matcher(lastLine);
        int lastMonth = 0;
        int lastDay = 0;
        if (lastMatcher.find()) {
            lastMonth = Integer.parseInt(lastMatcher.group("month"));
            lastDay = Integer.parseInt(lastMatcher.group("day"));
        }
        int lastDays = Date.dateToDays(lastMonth, lastDay);
        int commandIndex = 0;
        for (int i = 1; i <= lastDays; i++) {
            // 更新
            if ((i - 1) % 3 == 0) {
                ArrangingLibrarian.updateShelf(Date.daysToDate(i));
            }
            // 执行当日指令
            while (commandIndex < commandCnt) {
                String line = commands.get(commandIndex);
                Matcher matcher = pattern2.matcher(line);
                if (matcher.find()) {
                    int month = Integer.parseInt(matcher.group("month"));
                    int day = Integer.parseInt(matcher.group("day"));
                    if (Date.dateToDays(month, day) != i) {
                        break;
                    } else {
                        String date = matcher.group("date");
                        String pid = matcher.group("pid");
                        String op = matcher.group("op");
                        String type = matcher.group("type");
                        String id = matcher.group("id");
                        if (op.equals("borrowed")) {
                            BorrowReturnLibrarian.getBorrowMap().
                                    putIfAbsent(pid, new ArrayList<>());
                            BorrowReturnLibrarian.borrow(date, pid, type, id);
                        } else if (op.equals("smeared")) {
                            BorrowReturnLibrarian.smear(pid, type, id);
                        } else if (op.equals("lost")) {
                            BorrowReturnLibrarian.lost(date, pid, type, id);
                        } else if (op.equals("returned")) {
                            BorrowReturnLibrarian.returnBook(date, pid, type, id);
                        }
                    }
                }
                commandIndex++;
            }
        }
    }
}
