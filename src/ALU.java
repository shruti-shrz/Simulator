import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class ALU {
    int[] latch;
    Registers r;
    Dictionary<String,Integer> opcodes;
    ArrayList<String> allLines;
    HashMap<String,Integer> base;
    static int counter;
    static ALU alu;
    static HashMap<String,Integer> labels;
    public ALU(BufferedReader f,ArrayList<String> all,HashMap<String,Integer> b,HashMap<String,Integer> Labels){
        r = Registers.getInstance();
        latch = r.getC();
        allLines = all;
        base = b;
        labels = Labels;
        counter = labels.get("main")+1;
    }


    public int executer(String[] arr, int ch){
        try{
            if(Integer.parseInt(arr[0]) == 0){
                int l,m;
                if(ch==0)
                {  l = r.getreg(Integer.parseInt(arr[2]));
                    m = r.getreg(Integer.parseInt(arr[3]));}
                else
                {  l = latch[Integer.parseInt(arr[2])];
                    m = latch[Integer.parseInt(arr[3])];
                }
                int n = l+m;
                counter++;
                return n;
            }
            if(Integer.parseInt(arr[0]) == 1){
                int l,m;
                if(ch==0)
                { l = r.getreg(Integer.parseInt(arr[2]));
                    m = r.getreg(Integer.parseInt(arr[3]));}
                else
                {  l = latch[Integer.parseInt(arr[2])];
                    m = latch[Integer.parseInt(arr[3])];
                }
                int n = l-m;
                counter++;
                return n;
            }
            if(Integer.parseInt(arr[0]) == 2){
                int l;
                if(ch==0)
                    l = r.getreg(Integer.parseInt(arr[2]));
                else
                    l = latch[Integer.parseInt(arr[2])];
                int n = l + Integer.parseInt(arr[3]);
                counter++;
                return n;
            }
            if(Integer.parseInt(arr[0]) == 3){
                int l;
                if(ch==0)
                    l = r.getreg(Integer.parseInt(arr[2]));
                else
                    l = latch[Integer.parseInt(arr[2])];
                int index = l + Integer.parseInt(arr[3]);
                counter++;
                return index;
            }
            if(Integer.parseInt(arr[0]) == 4){
                int l=0;
                int m=0;
                if(ch==0) {
                    l = r.getreg(Integer.parseInt(arr[2]));
                    m = r.getreg(Integer.parseInt(arr[3]));}
                else
                    l = latch[Integer.parseInt(arr[2])];
                    m = latch[Integer.parseInt(arr[3])];
                int n;
                if(l < m){
                    n = 1;
                }
                else
                {
                    n=0;
                }
                counter++;
                return n;
            }
            if(Integer.parseInt(arr[0]) == 5){
                int l,m;
                if(ch==0)
                {l = r.getreg(Integer.parseInt(arr[1]));
                    m = r.getreg(Integer.parseInt(arr[2]));}
                else
                {l = latch[Integer.parseInt(arr[1])];
                    m = latch[Integer.parseInt(arr[2])];}
                if( l!= m){
                    counter = labels.get(arr[3])+1;
                }else
                    counter++;
                return -1;
            }
            if(Integer.parseInt(arr[0]) == 6){
                int l,m;
                if(ch==0)
                { l = r.getreg(Integer.parseInt(arr[1]));
                    m = r.getreg(Integer.parseInt(arr[2]));}
                else
                {l = latch[Integer.parseInt(arr[1])];
                    m = latch[Integer.parseInt(arr[2])];}
                if(l == m){
                    int t = labels.get(arr[3]);
                    counter = t+1;
                }else
                    counter++;
                return -1;
            }
            if(Integer.parseInt(arr[0]) == 7){
                counter = labels.get(arr[1])+1;
                return -1;
            }
            if(Integer.parseInt(arr[0])==9){
                int n = Integer.parseInt(arr[2]);
                counter++;
                return n;
            }
            if(Integer.parseInt(arr[0]) == 10) {
                int l;
                if(ch==0)
                    l = r.getreg(Integer.parseInt(arr[2]));
                else
                    l = latch[Integer.parseInt(arr[2])];
                int n =  l + Integer.parseInt(arr[3]);
                counter++;
                return n;
            }
            if(Integer.parseInt(arr[0]) == 11){
                int n = base.get(arr[2]);
                counter++;
                return n;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    int[] getReg(){
        return latch;
    }
    public static synchronized ALU getInstance(BufferedReader f,ArrayList<String> all,HashMap<String,Integer> b,HashMap<String,Integer> labels){
        if(alu == null)
        {
            alu = new ALU(f,all,b,labels);
        }
        return alu;
    }
}

