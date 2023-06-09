import java.util.ArrayList;
import java.util.HashSet;

public class OrderingLibrarian {
    private Shelf shelf;
    private ArrayList<Command> orderList;
    private HashSet<Command> invalidOrders;

    public OrderingLibrarian(Shelf shelf) {
        this.shelf = shelf;
        this.orderList = new ArrayList<>();
        this.invalidOrders = new HashSet<>();
    }

    public ArrayList<Command> getOrderList() {
        return orderList;
    }

    public HashSet<Command> getInvalidOrders() {
        return invalidOrders;
    }

    public boolean canOrder(Command command) {
        String date = command.getDate();
        String person = command.getPerson();
        String type = command.getType();
        String id = command.getId();
        //任何时候同一人对于相同书号图书的预定 仅有第一次被接受
        for (Command command1 : orderList) {
            if (command1.getPerson().equals(person) &&
                    command1.getType().equals(type) && command1.getId().equals(id)) {
                return false;
            }
        }
        //一天之内同一人仅允许预定最多三本书的副本，更多的预订 不会被接受
        int cnt = 0;
        for (Command command1 : orderList) {
            if (command1.getPerson().equals(person) && command1.getDate().equals(date)) {
                cnt++;
            }
        }
        if (cnt >= 3) {
            return false;
        }
        return Controller.canBorrow(person, type, id);
    }

    public void order(Command command) {
        if (canOrder(command)) {
            String date = command.getDate();
            String person = command.getPerson();
            String school = command.getSchool();
            String type = command.getType();
            String id = command.getId();
            orderList.add(command);
            //System.out.printf("[YYYY-mm-dd] <学校名称>-<学号> ordered <学校名称>-<类别号-序列号> from <服务部门>\n");
            System.out.printf("%s %s ordered %s-%s-%s from %s\n",
                    date, person, school, type, id, this);
            //System.out.printf("[YYYY-mm-dd] ordering librarian recorded
            // <学校名称>-<学号>'s order of <学校名称>-<类别号-序列号>\n");
            System.out.printf("%s ordering librarian recorded " +
                    "%s's order of %s-%s-%s\n", date, person, school, type, id);
            orderNewBook();
            //本校预定：如果该校从未有过该书，则购买。初始化数+已订购且到货数-丢失数=0才购买，否则仅预定。
            if (!shelf.ownedEver(type, id)) {
                command.setNeedBuy(true);
            }
        }
    }

    public void addInvalidBOrders(String person) {
        for (Command command : orderList) {
            if (command.getPerson().equals(person) && command.getType().equals("B")) {
                invalidOrders.add(command);
            }
        }
    }

    public void addInvalidCOrders(String person, String id) {
        for (Command command : orderList) {
            if (command.getPerson().equals(person) && command.getType().equals("C")
                    && command.getId().equals(id)) {
                invalidOrders.add(command);
            }
        }
    }

    public void removeInvalidOrders() {
        orderList.removeAll(invalidOrders);
        invalidOrders.clear();
    }

    public void borrow(Command command, Book book, String date) {
        String person = command.getPerson();
        String school = command.getSchool();
        String type = book.getType();
        String id = book.getId();
        //System.out.printf("[YYYY-mm-dd] <服务部门> lent <学校名称>-<类别号-序列号> to <学校名称>-<学号>\n");
        System.out.printf("%s %s lent %s-%s-%s to %s\n",
                date, this, school, type, id, person);
        if (Controller.isNeedStateOutput()) {
            book.lend();
        }
        getOrderedBook();
        //System.out.printf("[YYYY-mm-dd] <学校名称>-<学号> borrowed <学校名称>-<类别号-序列号> from <服务部门>\n");
        System.out.printf("%s %s borrowed %s-%s-%s from %s\n",
                date, person, school, type, id, this);
        book.setLimitDay(date);
        Controller.getpBooks().get(person).add(book);
        if (type.equals("B")) {
            addInvalidBOrders(person);
        }
    }

    public void orderNewBook() {
        if (Controller.isNeedStateOutput()) {
            System.out.printf("(Sequence) %s OrderingLibrarian sends " +
                    "a message to OrderingLibrarian\n", Controller.getDate());
        }
    }

    public void getOrderedBook() {
        if (Controller.isNeedStateOutput()) {
            System.out.printf("(Sequence) %s OrderingLibrarian sends " +
                    "a message to OrderingLibrarian\n", Controller.getDate());
        }
    }

    @Override
    public String toString() {
        return "ordering librarian";
    }
}
