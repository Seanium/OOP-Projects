public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) {         //数字
            curToken = getNumber();
        } else if (c == '(' || c == ')') {   //括号 // TODO 考虑合并单字符集合
            pos += 1;
            curToken = String.valueOf(c);
        } else if (c == '*' && input.charAt(pos + 1) == '*') { //TODO pos+1是否溢出 因为*绝对不是结尾字符，所以不会溢出
            pos += 2;
            curToken = "**";
        } else if (c == '*' && input.charAt(pos + 1) != '*') { //乘号
            pos += 1;
            curToken = "*";
        } else if (c == '+' || c == '-') {  //加减
            pos += 1;
            curToken = String.valueOf(c);
        } else if (c == 'x' || c == 'y' || c == 'z') {  //变量名 xyz
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public String peek() {
        return this.curToken;
    }
}
