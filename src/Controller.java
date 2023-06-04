import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private static ArrayList<String> commands = new ArrayList<>();
    private static int commandCnt;
    private static ArrayList<String> schools = new ArrayList<>();
    private static HashMap<String, Library> libraries = new HashMap<>();
    private static HashMap<String, ArrayList<Book>> pBooks = new HashMap<>();
    private static ArrayList<Command> waitList = new ArrayList<>();
    private static ArrayList<Book> outBooks = new ArrayList<>(); //校际借阅出借队列
    private static ArrayList<Book> returnBooks = new ArrayList<>(); //校际借阅归还队列

    public static ArrayList<Book> getReturnBooks() {
        return returnBooks;
    }

    public static HashMap<String, ArrayList<Book>> getpBooks() {
        return pBooks;
    }

    public static ArrayList<Command> getWaitList() {
        return waitList;
    }

    public static void getInput() {
        Scanner scanner = new Scanner(System.in);
        String regex0 = "(?<school>\\w+) (?<n>\\d+)";
        Pattern pattern0 = Pattern.compile(regex0);
        String regex1 = "(?<type>\\w)-(?<id>\\d+) (?<cnt>\\d+) (?<allowOut>[YN])";
        Pattern pattern1 = Pattern.compile(regex1);
        //初始化书
        int schoolCnt = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < schoolCnt; i++) {
            String line0 = scanner.nextLine();
            Matcher matcher0 = pattern0.matcher(line0);
            if (matcher0.find()) {
                //创建图书馆
                String school = matcher0.group("school");
                Library library = new Library(school);
                libraries.put(school, library);
                schools.add(school);
                int n = Integer.parseInt(matcher0.group("n"));
                for (int j = 0; j < n; j++) {
                    String line1 = scanner.nextLine();
                    Matcher matcher1 = pattern1.matcher(line1);
                    if (matcher1.find()) {
                        //上架书
                        library.initBooks(matcher1);
                    }
                }
            }
        }
        //读入操作
        commandCnt = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < commandCnt; i++) {
            commands.add(scanner.nextLine());
        }
    }

    public static void runCommands() {
        Command lastCommand = new Command(commands.get(commandCnt - 1));
        int days = Date.dateToDays(Integer.parseInt(lastCommand.getMonth()),
                Integer.parseInt(lastCommand.getDay()));
        int commandIndex = 0;
        for (int i = 1; i <= days; i++) {
            String date = Date.daysToDate(i);
            beforeOpen(date); //每天开馆前的工作
            //如果是整理日，图书管理处确认本校购入图书，然后整理管理员整理图书，之后预定管理员发放校内预定图书
            if ((i - 1) % 3 == 0) {
                for (String school : schools) {
                    libraries.get(school).getPurchasingDepartment().buy(date); //图书管理处确认本校购入图书
                }
                //System.out.printf("[YYYY-mm-dd] arranging librarian arranged all the books\n");
                System.out.printf("%s arranging librarian arranged all the books\n", date);
                for (String school : schools) {
                    libraries.get(school).getArrangingLibrarian().arrange(date); //整理管理员整理图书
                }
            }
            //执行当日指令
            waitList.clear(); //每天清空 waitList
            while (commandIndex < commandCnt) {
                Command command = new Command(commands.get(commandIndex));
                if (Date.dateToDays(Integer.parseInt(command.getMonth()),
                        Integer.parseInt(command.getDay())) != i) {
                    break;
                } else {
                    String op = command.getOp();
                    if (op.equals("borrowed")) {
                        libraries.get(command.getSchool()).borrow(command);
                    } else if (op.equals("smeared")) {
                        smear(command);
                    } else if (op.equals("lost")) {
                        lost(command);
                    } else if (op.equals("returned")) {
                        libraries.get(command.getSchool()).returnBook(command);
                    }
                }
                commandIndex++;
            }
            processOutOrder(); //校际借阅
            processLocalOrder(); //非校际借阅，即预定，可能预购
            //图书管理处发送校级借阅出借图书
            for (Book book : outBooks) {
                //System.out.printf("[YYYY-mm-dd] <学校名称>-<类别号-序列号> got transported
                // by <服务部门> in <学校名称>\n");
                System.out.printf("%s %s-%s-%s got transported by purchasing department in %s\n",
                        date, book.getSchool(), book.getType(), book.getId(), book.getSchool());
            }
            //图书管理处发送校级借阅归还图书
            for (Book book : returnBooks) {
                //System.out.printf("[YYYY-mm-dd] <学校名称>-<类别号-序列号> got transported
                // by <服务部门> in <学校名称>\n");
                System.out.printf("%s %s-%s-%s got transported by purchasing department in %s\n",
                        date, book.getSchool(), book.getType(), book.getId(), book.getOutSchool());
            }
        }

    }

    public static void beforeOpen(String date) {
        //图书管理处接收校际借阅外校归还图书
        for (Book book : returnBooks) {
            //System.out.printf("[YYYY-mm-dd] <学校名称>-<类别号-序列号> got received
            // by <服务部门> in <学校名称>\n");
            System.out.printf("%s %s-%s-%s got received by purchasing department in %s\n",
                    date, book.getSchool(), book.getType(), book.getId(), book.getSchool());
            book.setOut(false, "", "", ""); //重设图书为非外借
            libraries.get(book.getSchool()).getPurchasingDepartment().addBook(book);
        }
        //图书管理处接收校际借阅外校出借图书
        for (Book book : outBooks) {
            //System.out.printf("[YYYY-mm-dd] <学校名称>-<类别号-序列号> got received
            // by <服务部门> in <学校名称>\n");
            System.out.printf("%s %s-%s-%s got received by purchasing department in %s\n",
                    date, book.getSchool(), book.getType(), book.getId(), book.getOutSchool());
        }
        //发放校际借阅图书
        for (Book book : outBooks) {
            String person = book.getOutPerson();
            String type = book.getType();
            String id = book.getId();
            //System.out.printf("[YYYY-mm-dd] <服务部门> lent <学校名称>-<类别号-序列号> to <学校名称>-<学号>\n");
            System.out.printf("%s purchasing department lent %s-%s-%s to %s\n",
                    date, book.getSchool(), type, id, book.getOutPerson());
            //System.out.printf("[YYYY-mm-dd] <学校名称>-<学号> borrowed <学校名称>-<类别号-序列号>
            // from <服务部门>\n");
            System.out.printf("%s %s borrowed %s-%s-%s from purchasing department\n",
                    date, book.getOutPerson(), book.getSchool(), book.getType(), book.getId());
            pBooks.get(book.getOutPerson()).add(book);
            //不再符合数目限制的校内预定会自动被取消（如果是购置新书的请求也不会被完成）
            OrderingLibrarian orderingLibrarian =
                    libraries.get(book.getOutSchool()).getOrderingLibrarian();
            if (type.equals("B")) {
                orderingLibrarian.addInvalidBOrders(person);
                orderingLibrarian.removeInvalidOrders();
            } else if (type.equals("C")) {
                orderingLibrarian.addInvalidCOrders(person, id);
                orderingLibrarian.removeInvalidOrders();
            }
        }
        returnBooks.clear();
        outBooks.clear();
    }

    public static void lost(Command command) {
        String person = command.getPerson();
        String type = command.getType();
        String id = command.getId();
        ArrayList<Book> books = pBooks.get(person);
        for (Book book : books) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                if (book.isSmeared()) { //如果损坏，忽略丢失操作（面向评测机）
                    return;
                }
                books.remove(book);
                //罚款，这里的学校不一定对应，但不影响
                libraries.get(book.getSchool()).getBorrowReturnLibrarian().punish(command);
                libraries.get(book.getSchool()).getShelf().removeOwnedBook(book); //从馆藏中删除
                return;
            }
        }
    }

    public static void smear(Command command) {
        String type = command.getType();
        String id = command.getId();
        ArrayList<Book> books = pBooks.get(command.getPerson());
        for (Book book : books) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                book.setSmeared(true);
                return;
            }
        }
    }

    public static void processOutOrder() {
        for (int j = 0; j < waitList.size(); j++) {
            Command command = waitList.get(j);
            String person = command.getPerson();
            String school = command.getSchool();
            String pid = command.getPid();
            String type = command.getType();
            String id = command.getId();
            if (canBorrow(person, type, id)) {
                for (Library library : libraries.values()) {
                    Book book = library.getShelf().queryOut(type, id);
                    if (book != null) {
                        book.setOut(true, person, school, pid);
                        library.getShelf().removeBook(book);
                        outBooks.add(book); //添加到派送队列
                        pBooks.get(person).add(book); //提前分配，因为校际借阅需要提前判断
                        waitList.remove(command);
                        j--;
                        break;
                    }
                }
            }
        }
        //删除上面的提前分配，因为预定不和校际借阅共享合法性判断
        for (Book book : outBooks) {
            pBooks.get(book.getOutPerson()).remove(book);
        }
    }

    public static void processLocalOrder() {
        HashMap<String, ArrayList<Command>> schoolWaitList = new HashMap<>();
        for (String school : schools) {
            schoolWaitList.put(school, new ArrayList<>());
        }
        for (Command command : waitList) {
            schoolWaitList.get(command.getSchool()).add(command);
        }
        for (String school : schools) {
            ArrayList<Command> waitList1 = schoolWaitList.get(school);
            for (Command command : waitList1) {
                //本校预定：如果该校从未有过该书，则购买。初始化数+已订购且到货数-丢失数=0才购买，否则仅预定
                libraries.get(command.getSchool()).order(command);
            }
        }
    }

    public static boolean canBorrow(String person, String type, String id) {
        ArrayList<Book> books = pBooks.get(person);
        if (type.equals("B")) {
            for (Book book : books) {
                if (book.getType().equals(type)) {
                    return false;
                }
            }
        } else if (type.equals("C")) {
            for (Book book : books) {
                if (book.getType().equals(type) && book.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }
}
