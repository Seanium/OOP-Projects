package expr;

import poly.Poly;

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
        sb.append("((");         //TODO 加括号是否正确
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append("+");
            sb.append(iter.next().toString());
        }
        sb.append(")").append("**").append(expo).append(")");
        return sb.toString();
    }
}