import java.util.ArrayList;

public class BorrowReturnLibrarian {
    private ArrayList<Book> books;
    private Shelf shelf;
    private OrderingLibrarian orderingLibrarian;
    private LogisticsDivision logisticsDivision;

    public BorrowReturnLibrarian(Shelf shelf, OrderingLibrarian orderingLibrarian,
                                 LogisticsDivision logisticsDivision) {
        this.books = new ArrayList<>();
        this.shelf = shelf;
        this.orderingLibrarian = orderingLibrarian;
        this.logisticsDivision = logisticsDivision;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void borrowTypeB(Command command, Book book) {
        String date = command.getDate();
        String person = command.getPerson();
        String school = command.getSchool();
        String type = command.getType();
        String id = command.getId();
        if (Controller.canBorrow(person, type, id)) {
            //System.out.printf("[YYYY-mm-dd] <服务部门> lent <学校名称>-<类别号-序列号> to <学校名称>-<学号>\n");
            System.out.printf("%s %s lent %s-%s-%s to %s\n",
                    date, this, school, type, id, person);
            if (Controller.isNeedStateOutput()) {
                book.lend();
            }
            //System.out.printf("[YYYY-mm-dd] <学校名称>-<学号> borrowed <学校名称>-<类别号-序列号> from <服务部门>\n");
            System.out.printf("%s %s borrowed %s-%s-%s from %s\n",
                    date, person, school, type, id, this);
            shelf.removeBook(book);
            Controller.getpBooks().get(person).add(book);
            //此前对于任何 B 类书籍的预定，将从此刻起被取消
            orderingLibrarian.addInvalidBOrders(person);
            orderingLibrarian.removeInvalidOrders();
        } else {
            //System.out.printf("[YYYY-mm-dd] <服务部门> refused lending <学校名称>-<类别号-序列号>
            // to <学校名称>-<学号>\n");
            System.out.printf("%s %s refused lending %s-%s-%s to %s\n",
                    date, this, school, type, id, person);
            shelf.removeBook(book);
            books.add(book);
            if (Controller.isNeedStateOutput()) {
                book.refuseLend();
            }
        }
    }

    public void returnTypeB(Command command, Book book) {
        //罚款
        if (book.isSmeared()) {
            punish(command);
        }
        //输出归还信息
        String date = command.getDate();
        String person = command.getPerson();
        String school = book.getSchool();
        String type = book.getType();
        String id = book.getId();
        //System.out.printf("[YYYY-mm-dd] <学校名称>-<学号> returned <学校名称>-<类别号-序列号> to <服务部门>\n");
        System.out.printf("%s %s returned %s-%s-%s to %s\n", date, person, school, type, id, this);
        //System.out.printf("[YYYY-mm-dd] <服务部门> collected <学校名称>-<类别号-序列号> from <学校名称>-<学号>\n");
        System.out.printf("%s %s collected %s-%s-%s from %s\n",
                date, this, school, type, id, person);
        if (Controller.isNeedStateOutput()) {
            book.collect();
        }
        //修复，书真正转移
        Controller.getpBooks().get(command.getPerson()).remove(book);
        if (book.isSmeared()) {
            logisticsDivision.repair(command, book);
            if (book.isLentOut()) {
                Controller.getReturnBooks().add(book);
            } else {
                logisticsDivision.addBook(book);
            }
        } else {
            if (book.isLentOut()) {
                Controller.getReturnBooks().add(book);
            } else {
                books.add(book);
            }
        }
    }

    public void punish(Command command) {
        String date = command.getDate();
        String person = command.getPerson();
        //System.out.printf("[YYYY-mm-dd] <学校名称>-<学号> got punished by <服务部门>\n");
        System.out.printf("%s %s got punished by %s\n", date, person, this);
        //System.out.printf("[YYYY-mm-dd] borrowing and returning librarian
        // received <学校名称>-<学号>'s fine\n");
        System.out.printf("%s borrowing and returning librarian received %s's fine\n",
                date, person);
    }

    @Override
    public String toString() {
        return "borrowing and returning librarian";
    }
}
