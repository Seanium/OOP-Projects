package expr;

import poly.Poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public ArrayList<Term> getTerms() {
        return terms;
    }

    private int expo;    //指数

    public int getExpo() {
        return expo;
    }

    public Expr() {
        this.terms = new ArrayList<>();
        this.expo = 1;      //默认指数为1
    }

    public Expr(ArrayList<Term> terms, int expo) {
        this.terms = terms;
        this.expo = expo;
    }

    public void setExpo(int expo) {
        this.expo = expo;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public Poly toPoly() {
        Poly res = new Poly(new ArrayList<>());
        for (Term i : this.getTerms()) {
            res = res.addPoly(i.toPoly());
        }
        res = res.powPoly(this.getExpo());      // 指数运算
        res = res.merge();                      // 合并同类项
        return res;
    }

    @Override
    public String toString() {
        Iterator<Term> iter = getTerms().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("((");
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append("+");
            sb.append(iter.next().toString());
        }
        sb.append(")").append("**").append(expo).append(")");
        return sb.toString();
    }

    public Expr deri(String var) {
        Number number = new Number(BigInteger.valueOf(this.expo));
        final Expr expr1 = new Expr(this.terms, this.expo - 1);
        final Expr expr2 = new Expr();
        expr2.setExpo(1);
        for (Term i : this.terms) { //遍历每一个term 求导得到每个terms 加入到expr2.terms
            expr2.getTerms().addAll(i.deri(var));
        }
        Term term = new Term();
        term.addFactor(number);
        term.addFactor(expr1);
        term.addFactor(expr2);
        Expr expr = new Expr();
        expr.addTerm(term);
        return expr;
    }
}