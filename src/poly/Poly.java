package poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Poly {
    private ArrayList<Basic> basicArrayList;    //标准项=基本项数组

    public ArrayList<Basic> getBasicArrayList() {
        return basicArrayList;
    }

    public Poly(ArrayList<Basic> basicArrayList) {
        this.basicArrayList = basicArrayList;
    }

    public Poly addPoly(Poly that) {
        ArrayList<Basic> resArrayList = new ArrayList<>();  //TODO 相加时进行同类项合并
        resArrayList.addAll(this.getBasicArrayList());
        resArrayList.addAll(that.getBasicArrayList());
        return new Poly(resArrayList);
    }

    public Poly mulPoly(Poly that) {
        ArrayList<Basic> resArrayList = new ArrayList<>();
        for (Basic i : this.getBasicArrayList()) {
            for (Basic j : that.getBasicArrayList()) {
                Basic basic =
                        new Basic(i.getCoef().multiply(j.getCoef()),
                                i.getXexpo() + j.getXexpo(),
                                i.getYexpo() + j.getYexpo(),
                                i.getZexpo() + j.getZexpo());   // 系数相乘，指数相加
                resArrayList.add(basic);
            }
        }
        return new Poly(resArrayList);
    }

    public Poly powPoly(int expo) {
        if (expo == 0) {
            ArrayList<Basic> resArraylist = new ArrayList<>();
            resArraylist.add(new Basic(BigInteger.ONE, 0, 0, 0));    //指数为0，返回1
            return new Poly(resArraylist);
        }
        Poly res = this;
        for (int i = 1; i < expo; i++) {    //TODO 检查幂次运算是否正确
            res = res.mulPoly(this);
        }
        return res;
    }

    public void negate() {  // 正负翻转 //TODO 是否改成返回Poly
        for (Basic i : this.getBasicArrayList()) {
            i.setCoef(i.getCoef().multiply(BigInteger.valueOf(-1)));
        }
    }

    public Poly merge() { //合并同类项
        ArrayList<Basic> resArraylist = new ArrayList<>();
        Poly res = new Poly(resArraylist);
        for (Basic i : this.getBasicArrayList()) {
            boolean found = false;
            if (i.getCoef().equals(BigInteger.ZERO)) {  //系数为0 统一改为0*x**0*y**0*z**0
                i.setXexpo(0);
                i.setYexpo(0);
                i.setZexpo(0);
            }
            for (Basic j : res.getBasicArrayList()) {
                if (i.similarTo(j)) {
                    found = true;
                    j.setCoef(j.getCoef().add(i.getCoef()));    //找到同类项，与同类项系数相加
                    break;
                }
            }
            if (!found) {
                res.getBasicArrayList().add(i); //未找到同类项，添加该项
            }
        }
        return res;
    }

    //    @Override
    //    public String toString() {
    //        Iterator<Basic> iter = basicArrayList.iterator();
    //        StringBuilder sb = new StringBuilder();
    //        sb.append(iter.next().toString());
    //        while (iter.hasNext()) {
    //            String s = iter.next().toString();
    //            if (s.equals("0")) {
    //                continue;                       //不输出+0
    //            } else if (s.startsWith("-")) {
    //                sb.append("-");
    //                sb.append(s.substring(1));
    //            } else {
    //                sb.append("+");
    //                sb.append(s);
    //            }
    //        }
    //        return sb.toString();
    //    }
    @Override
    public String toString() {
        Iterator<Basic> iter = basicArrayList.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append("+");
            sb.append(iter.next().toString());
        }
        return sb.toString();
    }
}
