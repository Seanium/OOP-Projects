package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Term {
    private final ArrayList<Factor> factors;
    private int symbol;

    public Term() {
        this.factors = new ArrayList<>();
        this.symbol = 1;        //默认为正
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public String toString() {      //TODO 待修改
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" *");
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                sb.append(" *");
            }
        }
        return sb.toString();
    }
}
