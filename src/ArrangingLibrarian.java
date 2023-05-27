import java.util.ArrayList;

public class ArrangingLibrarian {
    private static ArrayList<Book> books = new ArrayList<>();

    public static void updateShelf(String date) {
        books.addAll(BorrowReturnLibrarian.getBooks());
        books.addAll(SelfServiceMachine.getBooks());
        books.addAll(LogisticsDivision.getBooks());
        BorrowReturnLibrarian.getBooks().clear();
        SelfServiceMachine.getBooks().clear();
        LogisticsDivision.getBooks().clear();

        for (Order order : OrderingLibrarian.getOrderList()) {
            if (!OrderingLibrarian.getInvalidOrders().contains(order)) {
                for (Book book : books) {
                    if (book.getType().equals(order.getType()) &&
                            book.getId().equals(order.getId())) {
                        OrderingLibrarian.getInvalidOrders().add(order);
                        books.remove(book); //之后立刻break，所以这里遍历删除不会出错
                        OrderingLibrarian.borrow(date, order.getPid(),
                                book.getType(), book.getId(), book);
                        break;
                    }
                }
            }
        }
        OrderingLibrarian.removeInvalidOrders();
        Shelf.addBooks(books);
        books.clear();
    }
}
