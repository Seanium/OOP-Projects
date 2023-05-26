import java.util.ArrayList;

public class LogisticsDivision {
    private static ArrayList<Book> books = new ArrayList<>();

    public static ArrayList<Book> getBooks() {
        return books;
    }

    public static void repairAndAdd(String date, String type, String id, Book book) {
        System.out.printf("%s %s-%s got repaired by logistics division\n", date, type, id);
        book.setSmeared(false);
        books.add(book);
    }
}
