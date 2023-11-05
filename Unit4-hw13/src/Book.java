public class Book {
    private String type;
    private String id;
    private boolean smeared = false;

    public Book(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setSmeared(boolean smeared) {
        this.smeared = smeared;
    }

    public boolean isSmeared() {
        return smeared;
    }
}
