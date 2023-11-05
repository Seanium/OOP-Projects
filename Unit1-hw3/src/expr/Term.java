package expr;

import poly.Basic;
import poly.Poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Term {
    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public int getSign() {
        return sign;
    }

    private final ArrayList<Factor> factors;
    private int sign;

    public Term() {
        this.factors = new ArrayList<>();
        this.sign = 1;        //默认为正
    }

    public Term reverseSign() {
        this.sign = -this.sign;
        return this;
    }

    public Term(ArrayList<Factor> factors, int sign) {
        this.factors = factors;
        this.sign = sign;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public Poly toPoly() {
        ArrayList<Basic> resArraylist = new ArrayList<>();
        resArraylist.add(new Basic(BigInteger.ONE, 0, 0, 0,
                new HashMap<>(), new HashMap<>()));    //1
        Poly res = new Poly(resArraylist);
        for (Factor i : this.getFactors()) {
            res = res.mulPoly(i.toPoly());
        }
        if (this.getSign() == -1) {             // 处理正负号
            res.negate();
        }
        return res;
    }

    @Override
    public String toString() {
        Iterator<Factor> iter = getFactors().iterator();
        StringBuilder sb = new StringBuilder();
        if (sign == -1) {        //重要的负号
            sb.append("-");
        }
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append("*");
            sb.append(iter.next().toString());
        }
        return sb.toString();
    }

    public ArrayList<Term> deri(String var) {
        ArrayList<Term> terms = new ArrayList<>();
        for (int i = 0; i < this.factors.size(); i++) {
            ArrayList<Factor> factors = new ArrayList<>(this.getFactors());
            factors.set(i, factors.get(i).deri(var));   //乘法法则，分别对每个因子求导
            Term term = new Term(factors, this.sign);
            terms.add(term);
        }
        return terms;
    }
}
