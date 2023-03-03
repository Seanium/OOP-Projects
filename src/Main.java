import expr.Expr;
import poly.Poly;
import tool.Definer;
import tool.Lexer;
import tool.Parser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 读入函数定义
        int num = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < num; i++) {
            String s = scanner.nextLine();
            Definer.defFunc(s);
        }
        String input = scanner.nextLine();
        Lexer lexer = new Lexer(input.replaceAll("[\t ]", ""));
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        Poly poly = expr.toPoly();
        System.out.println(poly.toString());
    }
}