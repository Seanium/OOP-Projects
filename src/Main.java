import expr.Expr;
import poly.Poly;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Lexer lexer = new Lexer(input.replaceAll("[\t ]", ""));
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        Poly poly = expr.toPoly();
        System.out.println(poly.toString());
    }
}