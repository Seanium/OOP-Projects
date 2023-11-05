import java.util.ArrayList;
import java.util.regex.Matcher;

public class Library {
    private String school;
    private Shelf shelf;
    private SelfServiceMachine selfServiceMachine;
    private BorrowReturnLibrarian borrowReturnLibrarian;
    private OrderingLibrarian orderingLibrarian;
    private PurchasingDepartment purchasingDepartment;
    private LogisticsDivision logisticsDivision;
    private ArrangingLibrarian arrangingLibrarian;

    public Library(String school) {
        this.school = school;
        this.shelf = new Shelf();
        this.logisticsDivision = new LogisticsDivision();
        this.orderingLibrarian = new OrderingLibrarian(shelf);
        this.borrowReturnLibrarian = new BorrowReturnLibrarian(shelf, orderingLibrarian,
                logisticsDivision);
        this.selfServiceMachine = new SelfServiceMachine(shelf,
                borrowReturnLibrarian, logisticsDivision);
        this.purchasingDepartment = new PurchasingDepartment(school, orderingLibrarian, shelf);
        this.arrangingLibrarian = new ArrangingLibrarian(borrowReturnLibrarian, selfServiceMachine,
                logisticsDivision, purchasingDepartment, orderingLibrarian, shelf);
    }

    public ArrangingLibrarian getArrangingLibrarian() {
        return arrangingLibrarian;
    }

    public BorrowReturnLibrarian getBorrowReturnLibrarian() {
        return borrowReturnLibrarian;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public OrderingLibrarian getOrderingLibrarian() {
        return orderingLibrarian;
    }

    public PurchasingDepartment getPurchasingDepartment() {
        return purchasingDepartment;
    }

    public void initBooks(Matcher matcher) {
        int cnt = Integer.parseInt(matcher.group("cnt"));
        boolean allowOut = matcher.group("allowOut").equals("Y");
        for (int k = 0; k < cnt; k++) {
            Book book = new Book(matcher.group("type"), matcher.group("id"), school, allowOut);
            shelf.addBook(book);
            shelf.addOwnedBook(book);
        }
    }

    public void borrow(Command command) {
        String person = command.getPerson();
        String type = command.getType();
        Controller.getpBooks().putIfAbsent(person, new ArrayList<>());
        Book book = selfServiceMachine.query(command);
        if (!type.equals("A")) {
            if (book != null) { //本校有余本
                if (type.equals("B")) {
                    borrowReturnLibrarian.borrowTypeB(command, book);
                } else if (type.equals("C")) {
                    selfServiceMachine.borrowTypeC(command, book);
                }
            } else { //本校无余本
                Controller.getWaitList().add(command);
            }
        }
    }

    public void order(Command command) {
        orderingLibrarian.order(command);
    }

    public void returnBook(Command command) {
        String person = command.getPerson();
        String type = command.getType();
        String id = command.getId();
        ArrayList<Book> books = Controller.getpBooks().get(person);
        for (Book book : books) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                if (type.equals("B")) {
                    borrowReturnLibrarian.returnTypeB(command, book);
                } else if (type.equals("C")) {
                    selfServiceMachine.returnTypeC(command, book);
                }
                break;
            }
        }
    }
}
