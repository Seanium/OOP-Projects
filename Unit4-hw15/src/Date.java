import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Date {
    private static int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static int dateToDays(int month, int day) {
        int days = day;
        for (int i = 1; i < month; i++) {
            days += daysInMonth[i];
        }
        return days;
    }

    public static String daysToDate(int days) {
        int preSum = 0;
        int sum = 0;
        for (int month = 0; month <= 12; month++) {
            sum += daysInMonth[month];
            if (sum >= days) {
                int day = days - preSum;
                return String.format("[2023-%02d-%02d]", month, day);
            }
            preSum = sum;
        }
        return null;
    }

    public static int stringDateToDays(String date) {
        String regex = "\\[(\\d+)-(?<month>\\d+)-(?<day>\\d+)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        if (matcher.find()) {
            int month = Integer.parseInt(matcher.group("month"));
            int day = Integer.parseInt(matcher.group("day"));
            return dateToDays(month, day);
        }
        return 0;
    }
}
