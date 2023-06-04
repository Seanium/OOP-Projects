import java.util.ArrayList;

public class LogisticsDivision {
    private ArrayList<Book> books;

    public LogisticsDivision() {
        this.books = new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void repair(Command command, Book book) {
        //System.out.printf("[YYYY-mm-dd] <学校名称>-<类别号-序列号> got repaired by <服务部门> in <学校名称>\n");
        System.out.printf("%s %s-%s-%s got repaired by %s in %s\n",
                command.getDate(), book.getSchool(), book.getType(), book.getId(),
                this, command.getSchool());
        if (Controller.isNeedStateOutput()) {
            book.repair();
        }
        book.setSmeared(false);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public String toString() {
        return "logistics division";
    }
}
