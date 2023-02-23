package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Expr implements Factor {
    private final ArrayList<Term> terms;
    private int symbol;   //正负号
    private BigInteger expo;    //指数

    public Expr() {
        this.terms = new ArrayList<>();
        this.symbol = 1;    //默认为正
        this.expo = new BigInteger("1");      //默认指数为1
    }

    public void setExpo(BigInteger expo) {
        this.expo = expo;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public String toString() {      //TODO 待修改
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" +");
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                sb.append(" +");
            }
        }
        return sb.toString();
    }
}
