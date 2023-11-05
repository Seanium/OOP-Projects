package expr;

import poly.Basic;
import poly.Poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Sincos implements Factor {
    private String type;    // sin还是cos
    private Factor factor;  // 括号内因子
    private int expo;       // 指数

    public Sincos(String type, Factor factor) {
        this.type = type;
        this.factor = factor;
        this.expo = 1;      // 指数默认为1
    }

    public void setExpo(int expo) {
        this.expo = expo;
    }

    @Override
    public Poly toPoly() {
        ArrayList<Basic> basicArrayList = new ArrayList<>();
        if (type.equals("sin")) {
            HashMap<Poly, Integer> sin = new HashMap<>();
            sin.put(factor.toPoly(), expo);   //TODO toPoly是否正确
            basicArrayList.add(new Basic(BigInteger.ONE, 0, 0, 0, sin, new HashMap<>()));
        } else if (type.equals("cos")) {
            HashMap<Poly, Integer> cos = new HashMap<>();
            cos.put(factor.toPoly(), expo);
            basicArrayList.add(new Basic(BigInteger.ONE, 0, 0, 0, new HashMap<>(), cos));
        }
        return new Poly(basicArrayList);
    }

    @Override
    public String toString() {
        return "(" + type + "(" + factor + ")**" + expo + ")";
    }
}
