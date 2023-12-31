import java.util.ArrayList;

public class SelfServiceMachine {
    private ArrayList<Book> books;
    private Shelf shelf;
    private BorrowReturnLibrarian borrowReturnLibrarian;
    private LogisticsDivision logisticsDivision;

    public SelfServiceMachine(Shelf shelf, BorrowReturnLibrarian borrowReturnLibrarian,
                              LogisticsDivision logisticsDivision) {
        this.books = new ArrayList<>();
        this.shelf = shelf;
        this.borrowReturnLibrarian = borrowReturnLibrarian;
        this.logisticsDivision = logisticsDivision;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public Book query(Command command) {
        String date = command.getDate();
        String person = command.getPerson();
        String type = command.getType();
        String id = command.getId();
        //System.out.printf("[YYYY-mm-dd] <学校名称>-<学号> queried <类别号-序列号> from <服务部门>\n");
        System.out.printf("%s %s queried %s-%s from %s\n",
                date, person, type, id, this);
        if (Controller.isNeedStateOutput()) {
            System.out.printf("(Sequence) %s SelfServiceMachine sends " +
                    "a message to SelfServiceMachine\n", date);
        }
        //System.out.printf("[YYYY-mm-dd] self-service machine provided information of
        // <类别号-序列号>\n");
        System.out.printf("%s self-service machine provided information of %s-%s\n",
                date, type, id);
        if (Controller.isNeedStateOutput()) {
            System.out.printf("(Sequence) %s SelfServiceMachine sends " +
                    "a message to SelfServiceMachine\n", date);
        }
        return shelf.query(type, id);
    }

    public void borrowTypeC(Command command, Book book) {
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
            if (Controller.isNeedStateOutput()) {
                System.out.printf("(Sequence) %s SelfServiceMachine sends " +
                        "a message to SelfServiceMachine\n", date);
            }
            //System.out.printf("[YYYY-mm-dd] <学校名称>-<学号> borrowed <学校名称>-<类别号-序列号> from <服务部门>\n");
            System.out.printf("%s %s borrowed %s-%s-%s from %s\n",
                    date, person, school, type, id, this);
            book.setLimitDay(date);
            shelf.removeBook(book);
            Controller.getpBooks().get(person).add(book);
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
            if (Controller.isNeedStateOutput()) {
                System.out.printf("(Sequence) %s SelfServiceMachine sends " +
                        "a message to SelfServiceMachine\n", date);
            }
        }
    }

    public void returnTypeC(Command command, Book book) {
        //罚款
        if (book.isSmeared()) {
            borrowReturnLibrarian.punish(command);
        }
        //输出归还信息
        String date = command.getDate();
        String person = command.getPerson();
        String school = book.getSchool();
        String type = book.getType();
        String id = book.getId();
        if (Date.stringDateToDays(date) > book.getLimitDay()) {
            borrowReturnLibrarian.punish(command);
        }
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

    @Override
    public String toString() {
        return "self-service machine";
    }
}
