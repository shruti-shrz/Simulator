import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.io.BufferedReader;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PreParser{
    ArrayList<String> all = new ArrayList<>();
    HashMap<String,Integer> base = new HashMap<>();
    Cache1 c1 = Cache1.getInstance();
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
                System.out.println(line);
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
                    memory.insert(Integer.parseInt(set[j]));
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
    ArrayList<String> allLines;
    static int length=0;
    JTextArea tarea;
    Memory m;
    int[] Registers;
    String[] arr;
    PreParser q;
    static int c=0;
   static String[] currInstr;
    int[] memlatch;
    int stall;
    int cycles;
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
            arr[2] = set[2].substring(3,set[2].length()-1);
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
        if((prv[1].equals(curr[2]) || prv[1].equals(curr[3])) && Integer.parseInt(prv[0])!= 5 && Integer.parseInt(prv[0])!= 6 && Integer.parseInt(curr[0])!= 9)// we have to remove branch cases
        {
            return true;
        }
        else
            return false;
    }
    static String[] prevInstr;
    int k;
    int no_of_instructions;
    public void startSimulation()
    {
        String line;
//        int stall=0;
        for(int i=0;i<prevInstr.length;i++)
            prevInstr[i] = "-1";
        System.out.print(alu.counter + " " + length + " ");
        while (alu.counter < length){
            line = allLines.get(alu.counter);
            currInstr = decodeinst(line); // stage 2, instruction decode
            if(currInstr[0].equals("-2"))
            {
                alu.counter++;

            }
            if(currInstr[0].equals("8")){
                break;
            }


            if((prevInstr[0] == "2" && areDependent(currInstr, prevInstr) == true) || (prevInstr[0] == "11" && areDependent(currInstr, prevInstr) == true))// here this stall is because of lw and la instructions
            {
                dataforwarding_memwb(); // dataforwarding from memwb latch
                stall++;
                no_of_instructions++;
            }else
            if (areDependent(currInstr, prevInstr) == true ) { // here dtaforwarding for RAW and no stall

                dataforwarding_exemem();// dataforwarding from exmem latch
                no_of_instructions++;
            }
            else
            if(currInstr[0]=="5" || currInstr[0]=="6")// here dataforwarding and 1 stall for all branch cases,
            {
                 // dataforwarding from exemem latch for branch instruction i.e. counter value of label
                stall++;
                no_of_instructions++;
                dataforwarding_exemem();
               // System.out.println(no_of_instructions + " branch ");
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
               // System.out.print("hello 123");
                k = alu.executer(currInstr,Registers);
                // stage 3 execute, execute for independent instructions
                no_of_instructions++;
            }
            for(int i=0;i<currInstr.length;i++)
                prevInstr[i] = currInstr[i];


            if(Integer.parseInt(currInstr[0])==3)
            {
                mem(k,currInstr);// mem stage 4
            }
            if(Integer.parseInt(currInstr[0])==2)
            {
                k =  mem(k,currInstr);
            }
            wb(k,currInstr);
          // write back stage 5
        }
       // System.out.print("hello ex 11");
        System.out.println(m.getMem());
        for(int i=0;i<Registers.length;i++)
            System.out.print(Registers[i] + " ");
        System.out.println();
        System.out.println("Stall " + stall);
        System.out.println("No. of instructions " + no_of_instructions);
        cycles = no_of_instructions + 4 + stall;
        System.out.println("No. of cycles " + cycles);
    }
    ALU alu;
    Parser(BufferedReader file){
        cycles = 0;
        stall = 0;
        q = new PreParser(file);
        tarea = q.tarea;
        allLines = q.getList();
        length = allLines.size();
        alu = ALU.getInstance(file,allLines,q.base,q.labels);
        Opcodes pt = Opcodes.getInstance();
        opcodes = pt.opt();
        arr = new String[4];
        currInstr = new String[4];
        prevInstr = new String[4];
        Registers = new int[32];
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
    int mem(int v,String[] g)
    {
        if(Integer.parseInt(g[0])==2)
        {
            memlatch = Registers;
            String add = toBinaryString(v,32);
            //System.out.print(add);
            return m.getMem().get(v);
        }
        else
        if(Integer.parseInt(g[0])==3)
        {
            m.getMem().set(v,Registers[Integer.parseInt(g[1])]);
        }
        return 0;
    }
    ALU getalu(){
        return alu;
    }
    int[] getReg()
    {
        return Registers;
    }
    void wb(int val,String[] g)
    {
        if(val!=-1)
        {

            Registers[Integer.parseInt(g[1])] = val;
        }
    }
    int exmem(String[] curr)
    {
        return alu.executer(curr,alu.latch);
    }
    int memwb(String[] curr)
    {
        return alu.executer(curr,memlatch);
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



