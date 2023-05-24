import java.util.ArrayList;

public class OrderingLibrarian {
    private static ArrayList<Order> orderList = new ArrayList<>();

    public static ArrayList<Order> getOrderList() {
        return orderList;
    }

    public static boolean canOrder(String date, String pid, String type, String id) {
        //任何时候同一人对于相同书号图书的预定 仅有第一次被接受
        for (Order order : orderList) {
            if (order.getPid().equals(pid) &&
                    order.getType().equals(type) && order.getId().equals(id)) {
                return false;
            }
        }
        //一天之内同一人仅允许预定最多三本书的副本，更多的预订 不会被接受
        int cnt = 0;
        for (Order order : orderList) {
            if (order.getPid().equals(pid) && order.getDate().equals(date)) {
                cnt++;
            }
        }
        if (cnt >= 3) {
            return false;
        }
        return BorrowReturnLibrarian.canBorrow(pid, type, id);
    }

    public static void order(String date, String pid, String type, String id) {
        if (canOrder(date, pid, type, id)) {
            System.out.printf("%s %s ordered %s-%s from ordering librarian\n", date, pid, type, id);
            orderList.add(new Order(date, pid, type, id));
        }
    }

    public static void borrow(String date, String pid, String type, String id, Book book) {
        System.out.printf("%s %s borrowed %s-%s from ordering librarian\n", date, pid, type, id);
        BorrowReturnLibrarian.getBorrowMap().get(pid).add(book);
        if (type.equals("B")) {
            for (Order order : orderList) {
                if (order.getPid().equals(pid) && order.getType().equals(type)) {
                    ArrangingLibrarian.getInvalidOrders().add(order);
                }
            }
        }
    }
}
