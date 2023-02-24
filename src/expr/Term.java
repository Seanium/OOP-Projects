package expr;

import poly.Basic;
import poly.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

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

    public void setSign(int sign) {
        this.sign = sign;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public Poly toPoly() {
        ArrayList<Basic> resArraylist = new ArrayList<Basic>();
        resArraylist.add(new Basic(BigInteger.valueOf(1), 0, 0, 0));    //1
        Poly res = new Poly(resArraylist);
        for (Factor i : this.getFactors()) {
            res = res.mulPoly(i.toPoly());
        }
        if (this.getSign() == -1) {             // 处理正负号
            res.negate();
        }
        return res;
    }
}
