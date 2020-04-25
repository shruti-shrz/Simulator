import java.lang.reflect.Array;
import java.util.*;

public class Opcodes {
    String line;
    String[] sl;
    static Opcodes op;
    Dictionary<String,List<String>> opt(){
        Dictionary<String,List<String>> dict = new Hashtable<>();
        ArrayList<String> a =new ArrayList<>(Arrays.asList("0","r","1","r","2","i","3","i","4","r","5","i","6","i","7","j","8","s","9","i","10","i","11","i"));
        dict.put("ADD",a.subList(0,2));
        dict.put("SUB",a.subList(2,4));
        dict.put("LW",a.subList(4,6));
        dict.put("SW",a.subList(6,8));
        dict.put("SLT",a.subList(8,10));
        dict.put("BNE",a.subList(10,12));
        dict.put("BEQ",a.subList(12,14));
        dict.put("J",a.subList(14,16));
        dict.put("SYSCALL",a.subList(16,18));
        dict.put("LI",a.subList(18,20));
        dict.put("ADDI",a.subList(20,22));
        dict.put("LA",a.subList(22,24));
        return dict;
    }
    public static synchronized Opcodes getInstance(){
        if(op==null)
        {
            op = new Opcodes();
        }
        return op;
    }

}
