package poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Poly {
    private ArrayList<Basic> basicArrayList;

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
        Poly res = new Poly(resArrayList);
        return res;
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
        Poly res = new Poly(resArrayList);
        return res;
    }

    public Poly powPoly(int expo) {
        if (expo == 0) {
            ArrayList<Basic> resArraylist = new ArrayList<Basic>();
            resArraylist.add(new Basic(BigInteger.ONE, 0, 0, 0));    //指数为0，返回1
            Poly res = new Poly(resArraylist);
            return res;
        }
        Poly res = this;
        for (int i = 1; i < expo; i++) {    //TODO 检查幂次运算是否正确
            res = res.mulPoly(this);
        }
        return res;
    }

    public void negate() {  //TODO 检查正负号取反运算是否正确
        for (Basic i : this.getBasicArrayList()) {
            i.setCoef(i.getCoef().multiply(BigInteger.valueOf(-1)));
        }
    }

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
