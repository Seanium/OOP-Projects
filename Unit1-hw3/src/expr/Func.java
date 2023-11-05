package expr;

import poly.Poly;
import tool.Definer;
import tool.Lexer;
import tool.Parser;

import java.util.ArrayList;

public class Func implements Factor {
    private String newFunc;     // 形参替换为实参之后的函数字符串
    private Expr expr;          // 函数表达式

    public Func(String name, ArrayList<Factor> args) {
        this.newFunc = Definer.callFunc(name, args);
        //System.out.println("Func.newFunc = " + newFunc);
        Lexer lexer = new Lexer(this.newFunc);
        Parser parser = new Parser(lexer);
        this.expr = parser.parseExpr();
        //System.out.println("Func.expr = " + expr);
    }

    @Override
    public Poly toPoly() {
        return this.expr.toPoly();
    }

    @Override
    public String toString() {
        return this.expr.toString();
    }

    public Expr deri(String var) {
        return this.expr.deri(var);
    }
}
