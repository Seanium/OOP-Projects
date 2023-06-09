import java.util.ArrayList;
import java.util.HashMap;

public class PurchasingDepartment {
    private ArrayList<Book> books;
    private String school;
    private OrderingLibrarian orderingLibrarian;
    private Shelf shelf;

    public PurchasingDepartment(String school, OrderingLibrarian orderingLibrarian,
                                Shelf shelf) {
        this.books = new ArrayList<>();
        this.school = school;
        this.orderingLibrarian = orderingLibrarian;
        this.shelf = shelf;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void buy(String date) {
        HashMap<String, Integer> buyNums = new HashMap<>();
        ArrayList<String> buySequence = new ArrayList<>();
        for (Command command : orderingLibrarian.getOrderList()) {
            if (command.isNeedBuy()) {
                String typeId = command.getTypeId();
                if (!buyNums.containsKey(typeId)) {
                    buyNums.put(typeId, 1);
                    buySequence.add(typeId);
                } else {
                    buyNums.replace(typeId, buyNums.get(typeId) + 1);
                }
            }
        }
        for (String typeId : buySequence) {
            if (buyNums.get(typeId) < 3) {
                buyNums.replace(typeId, 3);
            }
            //System.out.printf("[YYYY-mm-dd] <学校名称>-<类别号-序列号> got purchased
            // by <服务部门> in <学校名称>\n");
            System.out.printf("%s %s-%s got purchased by %s in %s\n",
                    date, school, typeId, this, school);
            String type = typeId.split("-")[0];
            String id = typeId.split("-")[1];
            for (int i = 0; i < buyNums.get(typeId); i++) {
                Book book = new Book(type, id, school, true);
                books.add(book);
                shelf.addOwnedBook(book); //更新馆藏
            }
        }
    }

    @Override
    public String toString() {
        return "purchasing department";
    }
}
