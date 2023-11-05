package expr;

import poly.Poly;

public interface Factor {
    Poly toPoly();

    String toString();

    Factor deri(String var);
}
