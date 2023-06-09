import java.util.ArrayList;

public class ArrangingLibrarian {
    private ArrayList<Book> books;
    private BorrowReturnLibrarian borrowReturnLibrarian;
    private SelfServiceMachine selfServiceMachine;
    private LogisticsDivision logisticsDivision;
    private PurchasingDepartment purchasingDepartment;
    private OrderingLibrarian orderingLibrarian;
    private Shelf shelf;

    public ArrangingLibrarian(BorrowReturnLibrarian borrowReturnLibrarian,
                              SelfServiceMachine selfServiceMachine,
                              LogisticsDivision logisticsDivision,
                              PurchasingDepartment purchasingDepartment,
                              OrderingLibrarian orderingLibrarian,
                              Shelf shelf) {
        this.books = new ArrayList<>();
        this.borrowReturnLibrarian = borrowReturnLibrarian;
        this.selfServiceMachine = selfServiceMachine;
        this.logisticsDivision = logisticsDivision;
        this.purchasingDepartment = purchasingDepartment;
        this.orderingLibrarian = orderingLibrarian;
        this.shelf = shelf;
    }

    public void arrange(String date) {
        books.addAll(borrowReturnLibrarian.getBooks());
        books.addAll(selfServiceMachine.getBooks());
        books.addAll(logisticsDivision.getBooks());
        books.addAll(purchasingDepartment.getBooks());
        borrowReturnLibrarian.getBooks().clear();
        selfServiceMachine.getBooks().clear();
        logisticsDivision.getBooks().clear();
        purchasingDepartment.getBooks().clear();
        for (Command command : orderingLibrarian.getOrderList()) {
            if (!orderingLibrarian.getInvalidOrders().contains(command)) {
                for (Book book : books) {
                    if (book.getType().equals(command.getType()) &&
                            book.getId().equals(command.getId())) {
                        orderingLibrarian.getInvalidOrders().add(command);
                        books.remove(book); //之后立刻break，所以一次循环里只删一次，不会出错
                        orderingLibrarian.borrow(command, book, date);
                        break;
                    }
                }
            }
        }
        orderingLibrarian.removeInvalidOrders();
        shelf.addBooks(books);
        books.clear();
    }
}
