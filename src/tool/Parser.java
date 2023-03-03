package tool;

import expr.Expr;
import expr.Factor;
import expr.Func;
import expr.Number;
import expr.Power;
import expr.Sincos;
import expr.Term;

import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    // 表达式 →→  [加减] 项 | 表达式 加减 项
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
        //System.out.println("expr = " + expr);
        return expr;
    }

    // 项 →→ [加减] 因子 | 项 '*' 因子
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

    // 因子 →→ 变量因子 | 常数因子 | 表达式因子
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
            Power power = new Power(lexer.peek());  //解析 x y z
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
        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {    //三角函数因子
            return parseSincos();
        } else if (lexer.peek().equals("f") || lexer.peek().equals("g")
                || lexer.peek().equals("h")) {      //自定义函数因子
            final String name = lexer.peek();
            lexer.next();           // 跳过 f g h
            lexer.next();           // 跳过 (
            ArrayList<Factor> factors = new ArrayList<>();
            factors.add(parseFactor());     //解析第一个因子
            while (lexer.peek().equals(",")) {
                lexer.next();               //跳过 ,
                factors.add(parseFactor()); //解析余下的因子
            }
            lexer.next();   //跳过 )
            return new Func(name, factors);
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

    public Sincos parseSincos() {   // 从parseFactor独立出parseSincos，减少方法长度
        String type = lexer.peek();         // 获得sin还是cos
        lexer.next();       // 跳过 sin cos
        lexer.next();       // 跳过 (
        Factor factor = parseFactor();
        Sincos sincos = new Sincos(type, factor);
        lexer.next();       // 跳过 )
        if (lexer.peek().equals("**")) {    //解析可能的**
            lexer.next();                   //跳过 **
            if (lexer.peek().equals("+")) { //解析指数前可能的正号
                lexer.next();               //跳过指数前正号
            }
            sincos.setExpo(Integer.parseInt(lexer.peek()));    //解析指数
            lexer.next();                   //跳过指数
        }
        return sincos;
    }
}