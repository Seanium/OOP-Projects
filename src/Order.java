public class Order {
    private String date;
    private String pid;
    private String type;
    private String id;

    public Order(String date, String pid, String type, String id) {
        this.date = date;
        this.pid = pid;
        this.type = type;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getPid() {
        return pid;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
