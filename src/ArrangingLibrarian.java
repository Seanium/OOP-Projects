import java.util.ArrayList;
import java.util.HashSet;

public class ArrangingLibrarian {
    private static ArrayList<Book> books = new ArrayList<>();
    private static HashSet<Order> invalidOrders = new HashSet<>();

    public static HashSet<Order> getInvalidOrders() {
        return invalidOrders;
    }

    public static void updateShelf(String date) {
        books.addAll(BorrowReturnLibrarian.getBooks());
        books.addAll(SelfServiceMachine.getBooks());
        books.addAll(LogisticsDivision.getBooks());
        BorrowReturnLibrarian.getBooks().clear();
        SelfServiceMachine.getBooks().clear();
        LogisticsDivision.getBooks().clear();

        for (Order order : OrderingLibrarian.getOrderList()) {
            if (!invalidOrders.contains(order)) {
                for (Book book : books) {
                    if (book.getType().equals(order.getType()) &&
                            book.getId().equals(order.getId())) {
                        invalidOrders.add(order);
                        books.remove(book);
                        OrderingLibrarian.borrow(date, order.getPid(),
                                book.getType(), book.getId(), book);
                        break;
                    }
                }
            }
        }
        OrderingLibrarian.getOrderList().removeAll(invalidOrders);
        invalidOrders.clear();
        Shelf.add(books);
    }
}
