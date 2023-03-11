package expr;

import poly.Basic;
import poly.Poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Power implements Factor {
    private String name;    //变量名 x/y/z
    private int expo;        //指数

    public Power(String name) {
        this.name = name;
        this.expo = 1;      //指数默认为1
    }

    public Power(String name, int expo) {
        this.name = name;
        this.expo = expo;
    }

    public void setExpo(int expo) {
        this.expo = expo;
    }

    public Poly toPoly() {
        ArrayList<Basic> basicArrayList = new ArrayList<>();
        if (name.equals("x")) {             // 1*(x**expo)*(y**0)*(z**0)
            basicArrayList.add(new Basic(BigInteger.ONE, expo, 0, 0,
                    new HashMap<>(), new HashMap<>()));
        } else if (name.equals("y")) {      // 1*(x**0)*(y**expo)*(z**0)
            basicArrayList.add(new Basic(BigInteger.ONE, 0, expo, 0,
                    new HashMap<>(), new HashMap<>()));
        } else if (name.equals("z")) {      // 1*(x**0)*(y**0)*(z**expo)
            basicArrayList.add(new Basic(BigInteger.ONE, 0, 0, expo,
                    new HashMap<>(), new HashMap<>()));
        }
        return new Poly(basicArrayList);
    }

    @Override
    public String toString() {
        return "(" + name + "**" + expo + ")";
    }

    public Expr deri(String var) {
        if (var.equals(this.name)) {
            Number number = new Number(BigInteger.valueOf(this.expo));
            Power power = new Power(this.name, (this.expo == 0) ? 0 : this.expo - 1);
            Term term = new Term();
            term.addFactor(number);
            term.addFactor(power);
            Expr expr = new Expr();
            expr.addTerm(term);
            return expr;
        } else {
            Number number = new Number(BigInteger.ZERO);
            Term term = new Term();
            term.addFactor(number);
            Expr expr = new Expr();
            expr.addTerm(term);
            return expr;
        }
    }
}
