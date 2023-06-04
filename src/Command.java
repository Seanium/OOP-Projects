import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    private String line;
    private String date;
    private String year;
    private String month;
    private String day;
    private String person;
    private String school;
    private String pid;
    private String op;
    private String typeId;
    private String type;
    private String id;
    private boolean needBuy;

    public Command(String line) {
        this.line = line;
        String regex = "(?<date>\\[(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)\\]) (?<person>" +
                "(?<school>\\w+)-(?<pid>\\d+)) (?<op>\\w+) (?<typeId>(?<type>\\w)-(?<id>\\d+))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            this.date = matcher.group("date");
            this.year = matcher.group("year");
            this.month = matcher.group("month");
            this.day = matcher.group("day");
            this.person = matcher.group("person");
            this.school = matcher.group("school");
            this.pid = matcher.group("pid");
            this.op = matcher.group("op");
            this.typeId = matcher.group("typeId");
            this.type = matcher.group("type");
            this.id = matcher.group("id");
            this.needBuy = false;
        }
    }

    public void setNeedBuy(boolean needBuy) {
        this.needBuy = needBuy;
    }

    public String getDate() {
        return date;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getPerson() {
        return person;
    }

    public String getSchool() {
        return school;
    }

    public String getPid() {
        return pid;
    }

    public String getOp() {
        return op;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public boolean isNeedBuy() {
        return needBuy;
    }
}
