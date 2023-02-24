import expr.Expr;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {  //TODO 调试用循环
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            Lexer lexer = new Lexer(input.replaceAll("[\t ]", ""));
            Parser parser = new Parser(lexer);
            Expr expr = parser.parseExpr();
            System.out.println(expr.toPoly().toString());
        }
    }
}