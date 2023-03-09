package poly;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Basic {
    private BigInteger coef;    //系数
    private int xexpo;         //指数
    private int yexpo;
    private int zexpo;
    private HashMap<Poly, Integer> sin;
    private HashMap<Poly, Integer> cos;

    public void setXexpo(int xexpo) {
        this.xexpo = xexpo;
    }

    public void setYexpo(int yexpo) {
        this.yexpo = yexpo;
    }

    public void setZexpo(int zexpo) {
        this.zexpo = zexpo;
    }

    public Basic(BigInteger coef, int xexpo, int yexpo, int zexpo,
                 HashMap<Poly, Integer> sin, HashMap<Poly, Integer> cos) {
        boolean sinZero = false;
        for (Poly poly : sin.keySet()) {
            if (poly.toString().equals("0")) {
                sinZero = true;
                break;
            }
        }
        this.coef = sinZero ? BigInteger.ZERO : coef;
        this.xexpo = xexpo;
        this.yexpo = yexpo;
        this.zexpo = zexpo;
        this.sin = sin;
        HashMap<Poly, Integer> cosNoZero = new HashMap<>(cos);
        for (Poly poly : cosNoZero.keySet()) {
            if (poly.toString().equals("0")) {
                cosNoZero.remove(poly);                 // cos(0) -> 1
            }
        }
        this.cos = cosNoZero;
    }

    public HashMap<Poly, Integer> getSin() {
        return sin;
    }

    public HashMap<Poly, Integer> getCos() {
        return cos;
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
                && this.getZexpo() == that.getZexpo()
                && this.getSin().equals(that.getSin())
                && this.getCos().equals(that.getCos());
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

    @Override
    public String toString() {
        if (this.getCoef().equals(BigInteger.ZERO)) {       // 系数为0
            return "0";
        }
        StringBuilder res = new StringBuilder(coef + simplifyUnit("x", xexpo)    // 系数非0
                + simplifyUnit("y", yexpo)
                + simplifyUnit("z", zexpo));
        for (Map.Entry<Poly, Integer> entry : this.getSin().entrySet()) {
            if (entry.getValue().equals(0)) {
                continue;
            }
            if (entry.getValue().equals(1)) {
                res.append("*sin((").append(entry.getKey().toString()).append("))");
            } else {
                res.append("*sin((").append(entry.getKey().toString()).append("))**")
                        .append(entry.getValue());
            }
        }
        for (Map.Entry<Poly, Integer> entry : this.getCos().entrySet()) {
            if (entry.getValue().equals(0)) {
                continue;
            }
            if (entry.getValue().equals(1)) {
                res.append("*cos((").append(entry.getKey().toString()).append("))");
            } else {
                res.append("*cos((").append(entry.getKey().toString()).append("))**")
                        .append(entry.getValue());
            }
        }
        //最后处理开头的1*或-1*
        if (res.toString().startsWith("1*")) {
            res = new StringBuilder(res.substring(2));
        } else if (res.toString().startsWith("-1*")) {
            res = new StringBuilder("-" + res.substring(3));
        }
        return res.toString();
    }

    //    @Override
    //    public String toString() {
    //        StringBuilder s;
    //        s = new StringBuilder(coef +
    //                "*x**" + xexpo +
    //                "*y**" + yexpo +
    //                "*z**" + zexpo);
    //        for (Map.Entry<Poly, Integer> entry : this.getSin().entrySet()) {
    //            s.append("*sin((").append(entry.getKey().toString())
    //                    .append("))**").append(entry.getValue());   //TODO 括号的处理是否正确
    //        }
    //        for (Map.Entry<Poly, Integer> entry : this.getCos().entrySet()) {
    //            s.append("*cos((").append(entry.getKey().toString()).
    //                    append("))**").append(entry.getValue());
    //        }
    //        return s.toString();
    //    }

}
