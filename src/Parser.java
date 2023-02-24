import expr.Expr;
import expr.Factor;
import expr.Number;
import expr.Power;
import expr.Term;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    // 表达式 → 空白项 [加减 空白项] 项 空白项 | 表达式 加减 空白项 项 空白项
    public Expr parseExpr() {
        Expr expr = new Expr();
        if (lexer.peek().equals("+")) {
            expr.setSign(1);
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            expr.setSign(-1);
            lexer.next();
        }
        expr.addTerm(parseTerm());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            //lexer.next();   //TODO 已解决BUG：此处不能有next，因为项的符号在parseTerm处理
            expr.addTerm(parseTerm());

        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.peek().equals("+")) {     //解析可能的正负号
            term.setSign(1);
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            term.setSign(-1);
            lexer.next();
        }
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) { //表达式因子
            lexer.next();               //跳过 (
            Expr expr = parseExpr();    //解析表达式
            lexer.next();               //跳过 )
            if (lexer.peek().equals("**")) {
                lexer.next();           //跳过 **
                if (lexer.peek().equals("+")) {
                    lexer.next();       //跳过指数可能的 +
                }
                expr.setExpo(Integer.parseInt(lexer.peek())); //解析指数
                lexer.next();           //跳过指数
            }
            return expr;
        } else if (lexer.peek().equals("x") || lexer.peek().equals("y")
                || lexer.peek().equals("z")) {  //幂函数因子
            Power power = new Power(lexer.peek());
            lexer.next();                       //跳过变量名
            if (lexer.peek().equals("**")) {
                lexer.next();                   //跳过 **
                if (lexer.peek().equals("+")) {
                    lexer.next();               //跳过指数可能的 +
                }
                power.setExpo(Integer.parseInt(lexer.peek()));    //解析指数
                lexer.next();                   //跳过指数
            }
            return power;
        } else {    //常数因子
            BigInteger num;
            if (lexer.peek().equals("-")) {         //负数
                lexer.next();   //跳过 -
                num = new BigInteger("-" + lexer.peek());
            } else if (lexer.peek().equals("+")) {  //无正号正数
                lexer.next();   //跳过可能的 +
                num = new BigInteger(lexer.peek());
            } else {                                //有正号正数
                num = new BigInteger(lexer.peek());
            }
            lexer.next();       //跳过纯数字
            return new Number(num);
        }
    }
}