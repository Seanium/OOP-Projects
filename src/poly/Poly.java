package poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Poly {
    private ArrayList<Basic> basicArrayList;    //标准项=基本项数组

    public ArrayList<Basic> getBasicArrayList() {
        return basicArrayList;
    }

    public Poly(ArrayList<Basic> basicArrayList) {
        this.basicArrayList = basicArrayList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Poly poly = (Poly) o;
        return this.toString().equals(poly.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.toString());
    }

    public Poly addPoly(Poly that) {
        ArrayList<Basic> resArrayList = new ArrayList<>();
        resArrayList.addAll(this.getBasicArrayList());
        resArrayList.addAll(that.getBasicArrayList());
        return new Poly(resArrayList);
    }

    public Poly mulPoly(Poly that) {
        ArrayList<Basic> resArrayList = new ArrayList<>();
        for (Basic i : this.getBasicArrayList()) {
            for (Basic j : that.getBasicArrayList()) {
                HashMap<Poly, Integer> sinres = new HashMap<>(i.getSin());
                j.getSin().forEach((key, value) ->
                        sinres.merge(key, value, Integer::sum));  // 合并sin的hashmap
                HashMap<Poly, Integer> cosres = new HashMap<>(i.getCos());
                j.getCos().forEach((key, value) ->
                        cosres.merge(key, value, Integer::sum));  // 合并cos的hashmap
                Basic basic =
                        new Basic(i.getCoef().multiply(j.getCoef()),
                                i.getXexpo() + j.getXexpo(),
                                i.getYexpo() + j.getYexpo(),
                                i.getZexpo() + j.getZexpo(),
                                sinres, cosres);   // 系数相乘，指数相加
                resArrayList.add(basic);
            }
        }
        return new Poly(resArrayList);
    }

    public Poly powPoly(int expo) {
        if (expo == 0) {
            ArrayList<Basic> resArraylist = new ArrayList<>();
            resArraylist.add(new Basic(BigInteger.ONE, 0, 0, 0,
                    new HashMap<>(), new HashMap<>()));    //指数为0，返回1
            return new Poly(resArraylist);
        }
        Poly res = this;
        for (int i = 1; i < expo; i++) {
            res = res.mulPoly(this);
        }
        return res;
    }

    public void negate() {  // 正负翻转
        for (Basic i : this.getBasicArrayList()) {
            i.setCoef(i.getCoef().multiply(BigInteger.valueOf(-1)));
        }
    }

    public Poly merge() { //合并同类项
        ArrayList<Basic> resArraylist = new ArrayList<>();
        Poly res = new Poly(resArraylist);
        //System.out.println(this.getBasicArrayList());
        for (Basic i : this.getBasicArrayList()) {
            //System.out.println(i);
            boolean found = false;
            if (i.getCoef().equals(BigInteger.ZERO)) {  //系数为0 统一改为0*x**0*y**0*z**0
                i.setXexpo(0);
                i.setYexpo(0);
                i.setZexpo(0);
            }
            for (Basic j : res.getBasicArrayList()) {
                //System.out.println(res.getBasicArrayList().indexOf(i));
                //System.out.println(res.getBasicArrayList().indexOf(j));
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

    @Override
    public String toString() {
        Iterator<Basic> iter = basicArrayList.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            String s = iter.next().toString();
            if (s.equals("0")) {
                continue;                       //不输出+0
            } else if (s.startsWith("-")) {
                sb.append("-");
                sb.append(s.substring(1));
            } else {
                sb.append("+");
                sb.append(s);
            }
        }

        if (sb.toString().startsWith("0+")) {           // "0+" -> ""
            sb.delete(0, 2);
        } else if (sb.toString().startsWith("0-")) {    // "0-" -> "-"
            sb.delete(0, 1);
        }
        String s = sb.toString();
        if (s.startsWith("-")) {
            int stack = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(') {
                    stack++;
                } else if (s.charAt(i) == ')') {
                    stack--;
                } else if (stack == 0 && s.charAt(i) == '+') {
                    s = s.substring(i + 1) + s.substring(0, i);
                    break;
                }
            }
        }
        s = s.replaceAll("(sin|cos)\\(\\(([xyz])\\)\\)", "$1($2)");     //双括号优化
        s = s.replaceAll("(sin|cos)\\(\\((-?\\d+)\\)\\)", "$1($2)");
        return s;
    }
    //    @Override
    //    public String toString() {
    //        Iterator<Basic> iter = basicArrayList.iterator();
    //        StringBuilder sb = new StringBuilder();
    //        sb.append(iter.next().toString());
    //        while (iter.hasNext()) {
    //            sb.append("+");
    //            sb.append(iter.next().toString());
    //        }
    //        return sb.toString();
    //    }
}
