package expr;

import poly.Basic;
import poly.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return this.num.toString();
    }

    public Poly toPoly() {
        ArrayList<Basic> basicArrayList = new ArrayList<>();
        basicArrayList.add(new Basic(num, 0, 0, 0));    // num*(x**0)*(y**0)*(z**0)
        return new Poly(basicArrayList);
    }
}
