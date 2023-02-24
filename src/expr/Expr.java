package expr;

import poly.Poly;

import java.util.ArrayList;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public ArrayList<Term> getTerms() {
        return terms;
    }

    private int sign;   //正负号

    public int getSign() {
        return sign;
    }

    private int expo;    //指数

    public int getExpo() {
        return expo;
    }

    public Expr() {
        this.terms = new ArrayList<>();
        this.sign = 1;    //默认为正
        this.expo = 1;      //默认指数为1
    }

    public void setExpo(int expo) {
        this.expo = expo;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public Poly toPoly() {
        Poly res = new Poly(new ArrayList<>());
        for (Term i : this.getTerms()) {
            res = res.addPoly(i.toPoly());
        }
        res = res.powPoly(this.getExpo());      // 指数运算 //TODO 已解决bug 少写了res= 导致res未改变
        if (this.getSign() == -1) {             // 处理正负号
            res.negate();
        }
        return res;
    }
}

