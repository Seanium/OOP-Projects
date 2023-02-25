package poly;

import java.math.BigInteger;

@SuppressWarnings("checkstyle:CommentsIndentation")
public class Basic {
    private BigInteger coef;    //系数
    private int xexpo;         //指数
    private int yexpo;
    private int zexpo;

    public void setXexpo(int xexpo) {
        this.xexpo = xexpo;
    }

    public void setYexpo(int yexpo) {
        this.yexpo = yexpo;
    }

    public void setZexpo(int zexpo) {
        this.zexpo = zexpo;
    }

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

    public boolean similarTo(Basic that) {  //判断是否同类项
        return this.getXexpo() == that.getXexpo()
                && this.getYexpo() == that.getYexpo()
                && this.getZexpo() == that.getZexpo();
    }

    public String simplifyUnit(String s, int expo) {
        if (expo == 0) {
            return "";
        }
        if (expo == 1) {
            return "*" + s;
        }
        if (expo == 2) {
            return "*" + s + "*" + s;
        } else {
            return "*" + s + "**" + expo;
        }
    }

    //    @Override
    //    public String toString() {
    //        if (this.getCoef().equals(BigInteger.ZERO)) {
    //            return "0";
    //        }
    //        if (this.getXexpo() == 0 && this.getYexpo() == 0 && this.getZexpo() == 0) {
    //            return String.valueOf(coef);
    //        }
    //        //最后优化系数为1或-1的情况
    //        String res = coef + simplifyUnit("x", xexpo)
    //                + simplifyUnit("y", yexpo)
    //                + simplifyUnit("z", zexpo);
    //        if (res.startsWith("1*")) {
    //            res = res.substring(2);
    //        } else if (res.startsWith("-1*")) {
    //            res = "-" + res.substring(3);
    //        }
    //        return res;
    //    }
    @Override
    public String toString() {
        return coef +
                "*x**" + xexpo +
                "*y**" + yexpo +
                "*z**" + zexpo;
    }

}
