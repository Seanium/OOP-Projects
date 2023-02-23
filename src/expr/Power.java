package expr;

import java.math.BigInteger;

public class Power implements Factor {
    private String name;    //变量名 x/y/z
    private BigInteger expo;        //指数

    public Power(String name) {
        this.name = name;
        this.expo = new BigInteger("1");      //指数默认为1
    }

    public void setExpo(BigInteger expo) {
        this.expo = expo;
    }
}
