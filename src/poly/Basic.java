package poly;

import java.math.BigInteger;

public class Basic {
    private BigInteger coef;    //系数
    private int xexpo;         //指数
    private int yexpo;
    private int zexpo;

    public Basic(BigInteger coef, int xexpo, int yexpo, int zexpo) {
        this.coef = coef;
        this.xexpo = xexpo;
        this.yexpo = yexpo;
        this.zexpo = zexpo;
    }

    public void setCoef(BigInteger coef) {
        this.coef = coef;
    }

    public BigInteger getCoef() {
        return coef;
    }

    public int getXexpo() {
        return xexpo;
    }

    public int getYexpo() {
        return yexpo;
    }

    public int getZexpo() {
        return zexpo;
    }

    @Override
    public String toString() {     //TODO 待修改
        return coef +
                "*x**" + xexpo +
                "*y**" + yexpo +
                "*z**" + zexpo;
    }
}
