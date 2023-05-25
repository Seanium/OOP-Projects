import java.util.ArrayList;
import java.util.HashMap;

public class BorrowReturnLibrarian {
    private static ArrayList<Book> books = new ArrayList<>();
    private static HashMap<String, ArrayList<Book>> borrowMap = new HashMap<>();

    public static HashMap<String, ArrayList<Book>> getBorrowMap() {
        return borrowMap;
    }

    public static ArrayList<Book> getBooks() {
        return books;
    }

    public static boolean canBorrow(String pid, String type, String id) {
        ArrayList<Book> borrowedBooks = borrowMap.get(pid);
        if (type.equals("B")) {
            for (Book book : borrowedBooks) {
                if (book.getType().equals(type)) {
                    return false;
                }
            }
        } else if (type.equals("C")) {
            for (Book book : borrowedBooks) {
                if (book.getType().equals(type) && book.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void borrow(String date, String pid, String type, String id) {
        Book book = SelfServiceMachine.query(date, pid, type, id);
        if (book != null) { //如果所需书籍目前有余本在架上
            if (type.equals("B")) {
                if (canBorrow(pid, type, id)) {
                    System.out.printf("%s %s borrowed %s-%s from " +
                            "borrowing and returning librarian\n", date, pid, type, id);
                    Shelf.remove(book);
                    borrowMap.get(pid).add(book);
                    OrderingLibrarian.addInvalidOrders(pid, type);
                    OrderingLibrarian.removeInvalidOrders();
                } else {
                    Shelf.remove(book);
                    books.add(book);
                }
            } else if (type.equals("C")) {
                SelfServiceMachine.borrow(date, pid, type, id, book);
            }
        } else { //如果所需书籍目前无余本在架上
            OrderingLibrarian.order(date, pid, type, id);
        }
    }

    public static void smear(String pid, String type, String id) {
        ArrayList<Book> borrowedBooks = borrowMap.get(pid);
        for (Book book : borrowedBooks) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                book.setSmeared(true);
                return;
            }
        }
    }

    public static void lost(String date, String pid, String type, String id) {
        //如果出现丢失，假设同学会立即到借还管理员处登记，并缴纳罚款
        punish(date, pid);
        ArrayList<Book> borrowedBooks = borrowMap.get(pid);
        for (Book book : borrowedBooks) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                borrowedBooks.remove(book);
                break;
            }
        }
    }

    public static void returnBook(String date, String pid, String type, String id) {
        ArrayList<Book> borrowedBooks = borrowMap.get(pid);
        for (Book book : borrowedBooks) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                if (type.equals("B")) {
                    if (book.isSmeared()) {
                        punish(date, pid);
                        System.out.printf("%s %s returned %s-%s to " +
                                "borrowing and returning librarian\n", date, pid, type, id);
                        borrowedBooks.remove(book);
                        LogisticsDivision.repairAndAdd(date, type, id, book);
                    } else {
                        System.out.printf("%s %s returned %s-%s to " +
                                "borrowing and returning librarian\n", date, pid, type, id);
                        borrowedBooks.remove(book);
                        books.add(book);
                    }
                } else if (type.equals("C")) {
                    SelfServiceMachine.returnBook(date, pid, type, id, book, borrowedBooks);
                }
                break;
            }
        }
    }

    public static void punish(String date, String pid) {
        System.out.printf("%s %s got punished by borrowing and returning librarian\n", date, pid);
    }
}
