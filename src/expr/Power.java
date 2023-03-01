package expr;

import poly.Basic;
import poly.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Power implements Factor {
    private String name;    //变量名 x/y/z
    private int expo;        //指数

    public Power(String name) {
        this.name = name;
        this.expo = 1;      //指数默认为1
    }

    public void setExpo(int expo) {
        this.expo = expo;
    }

    public Poly toPoly() {
        ArrayList<Basic> basicArrayList = new ArrayList<>();
        if (name.equals("x")) {             // 1*(x**expo)*(y**0)*(z**0)
            basicArrayList.add(new Basic(BigInteger.valueOf(1), expo, 0, 0));
        } else if (name.equals("y")) {      // 1*(x**0)*(y**expo)*(z**0)
            basicArrayList.add(new Basic(BigInteger.valueOf(1), 0, expo, 0));
        } else if (name.equals("z")) {      // 1*(x**0)*(y**0)*(z**expo)
            basicArrayList.add(new Basic(BigInteger.valueOf(1), 0, 0, expo));
        }
        return new Poly(basicArrayList);
    }
}
