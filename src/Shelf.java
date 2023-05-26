import java.util.ArrayList;

public class Shelf {
    private static ArrayList<Book> books = new ArrayList<>();

    public static void add(Book book) {
        books.add(book);
    }

    public static void addBooks(ArrayList<Book> newBooks) {
        books.addAll(newBooks);
    }

    public static void remove(Book book) {
        books.remove(book);
    }

    public static Book query(String type, String id) {
        for (Book book : books) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }
}
