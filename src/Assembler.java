import javax.swing.*;
import java.io.BufferedReader;
import java.util.*;

import static java.lang.Integer.parseInt;

class PreParser{
    ArrayList<String> all = new ArrayList<>();
    HashMap<String,Integer> base = new HashMap<>();
    //Cache1 c1 = Cache1.getInstance();
    Memory memory = Memory.getInstance();
    HashMap<String,Integer> labels = new HashMap<>();
    int lineNum = 0;
    JTextArea tarea = new JTextArea();
    JButton b2 = new JButton();
    PreParser(BufferedReader file){
        String line;
        try{
            while((line = file.readLine()) != null){
                line = line.trim();
                tarea.append(line+'\n');
                all.add(line);
                if (line.length() != 0) {
                    if(line.charAt(line.length() - 1) == ':') {

                        line = line.substring(0, line.length() - 1);

                        labels.put(line, lineNum);
                    }
                    lineNum++;
                }
            }
            this.storeMem(all);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    void storeMem(ArrayList<String> val){
        String[] set;
        int mc = 0;
        for(int i =1;i<textIndex(val);i++){
            if(val.get(i).charAt(val.get(i).length()-1)==':') {
                set = val.get(i + 1).split("[ ,]+");
                base.put(val.get(i).substring(0,val.get(i).length()-1),mc);
                for (int j = 1; j < set.length; j++) {
                    memory.insert(parseInt(set[j]));
                    mc++;
                }
            }
        }
    }
    int textIndex(ArrayList<String> values){
        for(int i=0;i<values.size();i++)
            if(values.get(i).equals(".text")){
                return i;
            }
        return -1;
    }
    public ArrayList<String> getList(){
        return all;
    }
}

class Parser{
    Dictionary<String,List<String>> opcodes;
    static Dictionary<String, ArrayList<Integer>> cache = new Hashtable();
    ArrayList<String> allLines;
    int tag_bit_c1=0;
    int tag_bit_c2=0;
    int index_bit_c1=0;
    int index_bit_c2=0;
    int off_bit_c1=0;
    int off_bit_c2=0;
    static int length=0;
    static int lw_sw=0;
    static double ipc =0.0;
    static int miss_in_c1,miss_in_c2,hit_c1,hit_c2;
    JTextArea tarea;
    Memory m;
    Cache1 c1;
    Cache2 c2;
    Registers r;
    String[] arr;
    PreParser q;
    static double miss_rate_1= 0.0;
    static double miss_rate_2 = 0.0;
   static double amat=0.0;
    static int hit1 = 4;
    static int hit2 = 10;
    static int miss_penalty2 = 100;
   static String[] currInstr;
    int[] memlatch;
    static int stall;
   static int cycles;
    String[] decodeinst(String line){
        String[] set = line.split("[ ,]+");
        List<String> l = opcodes.get(set[0].toUpperCase());
        if(l==null){
            arr[0] = "-2";
        }else
        if(l.get(1)=="r")
        {
            arr[0] = l.get(0);
            arr[1] = set[1].substring(1);
            arr[2] = set[2].substring(1);
            arr[3] = set[3].substring(1);
        }
        else
        if((l.get(1)=="i" && l.get(0) == "2") || (l.get(1)=="i" && l.get(0) == "3"))
        {
            int t = set[2].indexOf('(');
            arr[0] = l.get(0);
            arr[1] = set[1].substring(1);
            arr[2] = set[2].substring(t+2,set[2].length()-1);
            arr[3] = set[2].substring(0,t);
        }else
        if((l.get(1)=="i" && l.get(0)=="9") || (l.get(1)=="i" && l.get(0)=="11"))
        {
            arr[0] = l.get(0);
            arr[1] = set[1].substring(1);
            arr[2] = set[2];
        }else
        if(l.get(1)=="i")
        {
            arr[0] = l.get(0);
            arr[1] = set[1].substring(1);
            arr[2] = set[2].substring(1);
            arr[3] = set[3];

        }else
        if(l.get(1)=="j"){
            arr[0] = l.get(0);
            arr[1] = set[1];
        }else
        if(l.get(1)=="s"){
            arr[0] = l.get(0);
        }

        return arr;
    }
    boolean areDependent(String[] curr, String[] prv)
    {
        if(prv[0].equals("-2"))
        {
            return false;
        }
        if((prv[1].equals(curr[2]) || prv[1].equals(curr[3])) && parseInt(prv[0])!= 5 && parseInt(prv[0])!= 6 && parseInt(curr[0])!= 9)// we have to remove branch cases
        {
            return true;
        }
        else
            return false;
    }
    static String[] prevInstr;
    int k;
   static int no_of_instructions;
    public void startSimulation()
    {
        String line;
        for(int i=0;i<prevInstr.length;i++)
            prevInstr[i] = "-1";
        while (alu.counter < length){
            line = allLines.get(alu.counter);
            currInstr = decodeinst(line);
            if(currInstr[0].equals("-2"))
            {
                alu.counter++;
                continue;
            }
            if(currInstr[0].equals("8")){
                break;
            }

            if((prevInstr[0] == "2" && areDependent(currInstr, prevInstr) == true) || (prevInstr[0] == "11" && areDependent(currInstr, prevInstr) == true))
            {
                dataforwarding_memwb();
                stall++;
                no_of_instructions++;
            }else
            if (areDependent(currInstr, prevInstr) == true )
            {
                dataforwarding_exemem();
                no_of_instructions++;
            }
            else
            if(currInstr[0]=="5" || currInstr[0]=="6")
            {
                stall++;
                no_of_instructions++;
                dataforwarding_exemem();
            }
            else
            if(currInstr[0]=="7")
            {
                dataforwarding_exemem();
                stall++;
                no_of_instructions++;
            }
            else
            {
                k = alu.executer(currInstr,0);
                no_of_instructions++;
            }
            for(int i=0;i<currInstr.length;i++)
                prevInstr[i] = currInstr[i];


            if(parseInt(currInstr[0])==3)
            {
                lw_sw++;
                mem(k,currInstr);
            }
            if(parseInt(currInstr[0])==2)
            {
                lw_sw++;
                k =  mem(k,currInstr);
            }
            wb(k,currInstr);
        }
        timeline();
        System.out.println(m.getMem());
        r.printreg();
        System.out.println("miss in 1 "+miss_in_c1);
        System.out.println("miss in 2 "+miss_in_c2);
        System.out.println("hit in 1 "+hit_c1);
        System.out.println("hit in 2 "+hit_c2);
        System.out.println("Stall " + stall);
        System.out.println("No. of instructions " + no_of_instructions);
        System.out.println("IPC " + ipc);
        System.out.println("miss rate one " + miss_rate_1);
        System.out.println("miss rate two " + miss_rate_2);
        System.out.println("AMAT " + amat);
        System.out.println("No. of cycles " + cycles);
    }
    ALU alu;
    ArrayList<Integer> ca1;
    ArrayList<Integer> ca2;
    Parser(BufferedReader file){
        cycles = 0;
        stall = 0;
        q = new PreParser(file);
        tarea = q.tarea;
        allLines = q.getList();
        length = allLines.size();
        miss_in_c1=miss_in_c2=hit_c1=hit_c2=0;
        alu = ALU.getInstance(file,allLines,q.base,q.labels);
        Opcodes pt = Opcodes.getInstance();
        opcodes = pt.opt();
        ca1 = new ArrayList(Arrays.asList(1024,4,2,4));
        ca2 = new ArrayList(Arrays.asList(4096,8,1,10,100));
        cache.put("cache1",ca1);
        cache.put("cache2",ca2);
        off_bit_c1 = (int)Math.ceil((Math.log(ca1.get(1)) / Math.log(2)));//2
        off_bit_c2 = (int)Math.ceil((Math.log(ca2.get(1)) / Math.log(2)));//3
        index_bit_c1 = (int)Math.ceil((Math.log(ca1.get(2)) / Math.log(2)));//1
        index_bit_c2 = (int)Math.ceil((Math.log(ca2.get(2)) / Math.log(2)));//0
        tag_bit_c1 = 32 - (index_bit_c1+off_bit_c1);//29
        tag_bit_c2 = 32 - (index_bit_c2+off_bit_c2);//29
        System.out.println(tag_bit_c1+" "+index_bit_c1+" "+off_bit_c1+"");
        System.out.println(tag_bit_c2+" "+index_bit_c2+" "+off_bit_c2+"");
        c1 = Cache1.getInstance(cache);
        c2 = Cache2.getInstance(cache);
        arr = new String[4];
        currInstr = new String[4];
        prevInstr = new String[4];
        r = Registers.getInstance();
        m = Memory.getInstance();
        memlatch = new int[32];
    }
    public static String toBinaryString(int a, int len)
    {
        if(len > 0)
        {
            return String.format("%" + len + "s", Integer.toBinaryString(a)).replaceAll(" ", "0");
        }
        return null;
    }
    int val;
     int Controller(String add,String[] g) {
        int l,n;
        if(index_bit_c1==0)
        l = c1.search(add.substring(0,tag_bit_c1),parseInt(add.substring(tag_bit_c1+index_bit_c1),2),0,add);
        else
            l = c1.search(add.substring(0,tag_bit_c1),parseInt(add.substring(tag_bit_c1+index_bit_c1),2),parseInt(add.substring(tag_bit_c1,(tag_bit_c1+index_bit_c1)),2),add);
        if(l==-1)
        {
            miss_in_c1++;
            if(index_bit_c2==0)
                n =  c2.search(add.substring(0,tag_bit_c2),parseInt(add.substring(tag_bit_c2+index_bit_c2),2),add,0);
            else
                n =  c2.search(add.substring(0,tag_bit_c2),parseInt(add.substring(tag_bit_c2+index_bit_c2),2),add,parseInt(add.substring(tag_bit_c2,(tag_bit_c2+index_bit_c2)),2));
           if(n==-1)
           {
               miss_in_c2++;
               if(g[0]=="2")
               {
                   val = m.get(parseInt(add,2));
                   if(index_bit_c1==0)
                   c1.insert(add.substring(0,tag_bit_c1),0,parseInt(add,2));
                   else
                       c1.insert(add.substring(0,tag_bit_c1),parseInt(add.substring(tag_bit_c1,(tag_bit_c1+index_bit_c1)),2),parseInt(add,2));
                   if(index_bit_c2==0)
                       c2.insert(add.substring(0,tag_bit_c2),0,parseInt(add,2));
                   else
                        c2.insert(add.substring(0,tag_bit_c2),parseInt(add.substring(tag_bit_c2,(tag_bit_c2+index_bit_c2)),2),parseInt(add,2));
                   return val;
               }
               if(g[0]=="3")
               {
                   if(index_bit_c1==0)
                       c1.insert(add.substring(0,tag_bit_c1),0,parseInt(add,2));
                   else
                       c1.insert(add.substring(0,tag_bit_c1),parseInt(add.substring(tag_bit_c1,(tag_bit_c1+index_bit_c1)),2),parseInt(add,2));
                   if(index_bit_c2==0)
                       c2.insert(add.substring(0,tag_bit_c2),0,parseInt(add,2));
                   else
                       c2.insert(add.substring(0,tag_bit_c2),parseInt(add.substring(tag_bit_c2,(tag_bit_c2+index_bit_c2)),2),parseInt(add,2));

                   c1.set(tag_bit_c2,index_bit_c2,parseInt(add.substring(tag_bit_c1+index_bit_c1),2),parseInt(add.substring(tag_bit_c1,(tag_bit_c1+index_bit_c1)),2),r.getreg(parseInt(g[1])),add);

                   return r.getreg(parseInt(g[1]));
               }
           }
           else
           {
               hit_c2++;
               if(g[0]=="2")
               {
                   val = n;
                   if(index_bit_c1==0)
                       c1.insert(add.substring(0,tag_bit_c1),0,parseInt(add,2));
                   else
                       c1.insert(add.substring(0,tag_bit_c1),parseInt(add.substring(tag_bit_c1,(tag_bit_c1+index_bit_c1)),2),parseInt(add,2));
                   return val;
               }
               if(g[0]=="3")
               {
                   val = n;
                   if(index_bit_c1==0)
                       c1.insert(add.substring(0,tag_bit_c1),0,parseInt(add,2));
                   else
                       c1.insert(add.substring(0,tag_bit_c1),parseInt(add.substring(tag_bit_c1,(tag_bit_c1+index_bit_c1)),2),parseInt(add,2));
                   c1.set(tag_bit_c2,index_bit_c2,parseInt(add.substring(tag_bit_c1+index_bit_c1),2),parseInt(add.substring(tag_bit_c1,(tag_bit_c1+index_bit_c1)),2),r.getreg(parseInt(g[1])),add);
                   return val;
               }
           }

        }
        else
        {
            hit_c1++;
            if(g[0]=="2") {
                val = l;
                return val;
            }
            if(g[0]=="3") {
                val = l;
                c1.set(tag_bit_c2,index_bit_c2,parseInt(add.substring(tag_bit_c1+index_bit_c1),2),parseInt(add.substring(tag_bit_c1,(tag_bit_c1+index_bit_c1)),2),r.getreg(parseInt(g[1])),add);
                return val;
            }
        }
        return 0;
    }
    static void timeline()
    {
       miss_rate_1 = (double)miss_in_c1/(double)(miss_in_c1+hit_c1);
       miss_rate_2 =(double)miss_in_c2/(double)(miss_in_c2 + hit_c2);
       amat = ((double) hit1 + miss_rate_1*((double) hit2+(miss_rate_2*(double)miss_penalty2)));
       cycles = (int) ((no_of_instructions-lw_sw) + 4 + lw_sw*amat + stall);
       ipc = (double)no_of_instructions/(double)cycles;
       stall = (int) (stall + (int)lw_sw*(amat-1));
    }

    int mem(int v,String[] g)
    {
        String add = toBinaryString(v,32);
        int val = Controller(add,g);
        return val;
    }
    ALU getalu(){
        return alu;
    }
    void wb(int val,String[] g)
    {
        if(val!=-1 && g[0]!="3")
        {
            r.insert(Integer.parseInt(g[1]),val);
        }
    }
    int exmem(String[] curr)
    {
        return alu.executer(curr,1);
    }
    int memwb(String[] curr)
    {
        return alu.executer(curr,1);
    }
    void dataforwarding_memwb()
    {
        k = memwb(currInstr);
    }
    void dataforwarding_exemem()
    {
        k = exmem(currInstr);
    }
}



