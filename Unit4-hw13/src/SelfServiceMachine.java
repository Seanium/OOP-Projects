import java.util.ArrayList;

public class SelfServiceMachine {
    private static ArrayList<Book> books = new ArrayList<>();

    public static ArrayList<Book> getBooks() {
        return books;
    }

    public static void add(Book book) {
        books.add(book);
    }

    public static Book query(String date, String pid, String type, String id) {
        System.out.printf("%s %s queried %s-%s from self-service machine\n", date, pid, type, id);
        return Shelf.query(type, id);
    }

    public static void borrow(String date, String pid, String type, String id, Book book) {
        if (BorrowReturnLibrarian.canBorrow(pid, type, id)) {
            System.out.printf("%s %s borrowed %s-%s from " +
                    "self-service machine\n", date, pid, type, id);
            Shelf.remove(book);
            BorrowReturnLibrarian.getBorrowMap().get(pid).add(book);
        } else {
            Shelf.remove(book);
            SelfServiceMachine.add(book);
        }
    }

    public static void returnBook(String date, String pid, String type, String id,
                                  Book book, ArrayList<Book> borrowedBooks) {
        if (book.isSmeared()) {
            BorrowReturnLibrarian.punish(date, pid);
            System.out.printf("%s %s returned %s-%s to " +
                    "self-service machine\n", date, pid, type, id);
            borrowedBooks.remove(book);
            LogisticsDivision.repairAndAdd(date, type, id, book);
        } else {
            System.out.printf("%s %s returned %s-%s to " +
                    "self-service machine\n", date, pid, type, id);
            borrowedBooks.remove(book);
            books.add(book);
        }
    }
}
