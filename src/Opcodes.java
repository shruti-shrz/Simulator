import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class Opcodes {
    String line;
    String[] sl;
    Dictionary<String,Integer> opt(){
        Dictionary<String,Integer> dict = new Hashtable<>();
        dict.put("ADD",0);
        dict.put("SUB",1);
        dict.put("LW",2);   
        dict.put("SW",3);
        dict.put("SLT",4);
        dict.put("BNE",5);
        dict.put("BEQ",6);
        dict.put("J",7);
        dict.put("SYSCALL",8);
        dict.put("LI",9);
        dict.put("ADDI",10);
        dict.put("LA",11);
        return dict;
    }


}
