package tool;

import expr.Factor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Definer {
    public Definer() {
        funcMap = new HashMap<>();
        paraMap = new HashMap<>();
    }

    private static HashMap<String, String> funcMap = new HashMap<>();  // 函数名: 定义式
    private static HashMap<String, ArrayList<String>> paraMap = new HashMap<>();    // 函数名: 形参列表

    public static HashMap<String, String> getFuncMap() {
        return funcMap;
    }

    public static HashMap<String, ArrayList<String>> getParaMap() {
        return paraMap;
    }

    public static void defFunc(String s) {     // 自定义函数定义
        String pattern = "(?<name>[fgh])\\((?<para1>[xyz])," +
                "?(?<para2>[xyz])?,?(?<para3>[xyz])?\\)=(?<formula>.+)";
        Pattern re = Pattern.compile(pattern);
        Matcher m = re.matcher(s);
        m.find();
        getFuncMap().put(m.group("name"), m.group("formula"));
        ArrayList<String> paras = new ArrayList<>();
        paras.add(m.group("para1"));
        if (m.group("para2") != null) {
            paras.add(m.group("para2"));
            if (m.group("para3") != null) {
                paras.add(m.group("para3"));
            }
        }
        getParaMap().put(m.group("name"), paras);
    }

    public static String callFunc(String name, ArrayList<Factor> args) {       // 自定义函数调用
        String formula = getFuncMap().get(name);           // 根据函数名找到定义式
        //System.out.println("形参formula = " + formula);
        ArrayList<String> paras = getParaMap().get(name);  // 根据函数名得到形参列表
        HashMap<String, String> paMap = new HashMap<>();        // 定义从形参到实参的映射
        for (int i = 0; i < paras.size(); i++) {                // 建立从形参到实参的映射
            paMap.put(paras.get(i), args.get(i).toString());    //TODO Factor.toString()是否需要重写
        }
        //System.out.println("paMap.size() = " + paMap.size());
        //System.out.println("paMap.get(\"x\") = " + paMap.get("x"));
        //System.out.println("paMap.get(\"y\") = " + paMap.get("y"));
        //System.out.println("paMap.get(\"z\") = " + paMap.get("z"));
        for (int i = 0; i < formula.length(); i++) {
            String arg = paMap.get(formula.substring(i, i + 1));
            if (arg != null) {
                formula = formula.substring(0, i) + arg + formula.substring(i + 1);   // 形参替换为实参
                i += arg.length();
            }
        }
        //System.out.println("实参formula = " + formula);
        return formula;
    }
}
