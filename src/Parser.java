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
        if (lexer.peek().equals("+")) {         //有正号项
            lexer.next();   //跳过正号
            expr.addTerm(parseTerm());
        } else if (lexer.peek().equals("-")) {  //有负号项
            lexer.next();   //跳过负号
            expr.addTerm(parseTerm().reverseSign());    //正负翻转
        } else {
            expr.addTerm(parseTerm());          //无号项
        }

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("+")) {         //有正号项
                lexer.next();   //跳过正号
                expr.addTerm(parseTerm());
            } else if (lexer.peek().equals("-")) {  //有负号项
                lexer.next();   //跳过负号
                expr.addTerm(parseTerm().reverseSign());    //正负翻转
            }
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.peek().equals("+")) {     //解析可能的正负号
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            term = term.reverseSign();
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
            if (lexer.peek().equals("**")) {    //解析可能的**
                lexer.next();           //跳过 **
                if (lexer.peek().equals("+")) { //解析指数前可能的正号
                    lexer.next();       //跳过指数前正号
                }
                expr.setExpo(Integer.parseInt(lexer.peek())); //解析指数
                lexer.next();           //跳过指数
            }
            return expr;
        } else if (lexer.peek().equals("x") || lexer.peek().equals("y")
                || lexer.peek().equals("z")) {  //幂函数因子
            Power power = new Power(lexer.peek());
            lexer.next();                       //跳过变量名
            if (lexer.peek().equals("**")) {    //解析可能的**
                lexer.next();                   //跳过 **
                if (lexer.peek().equals("+")) { //解析指数前可能的正号
                    lexer.next();               //跳过指数前正号
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
            } else if (lexer.peek().equals("+")) {  //有正号正数
                lexer.next();   //跳过可能的 +
                num = new BigInteger(lexer.peek());
            } else {                                //无正号正数
                num = new BigInteger(lexer.peek());
            }
            lexer.next();       //跳过纯数字
            return new Number(num);
        }
    }
}