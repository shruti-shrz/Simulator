import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PreParser{
    ArrayList<String> all = new ArrayList<>();
    HashMap<String,Integer> labels = new HashMap<>();
    int lineNum = 0;
    PreParser(BufferedReader file){
        String line;
        try{
            while ((line = file.readLine()) != null) {
//                   System.out.println(line);
                line = line.trim();

                if (line.length() != 0) {
                    all.add(line);
                    if (line.charAt(line.length() - 1) == ':') {

                        line = line.substring(0, line.length() - 1);

                        labels.put(line, lineNum);
                    }
                    lineNum++;
                    // System.out.println(labels);

                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    public ArrayList<String> getList(){
        return all;
    }
}

class Parser{
    static ArrayList<Integer> memory = new ArrayList<>();
    static HashMap<String,Integer> base = new HashMap<>();
    Dictionary<String,Integer> opcodes;
    ArrayList<String> allLines;
    PreParser q;
    int[] Registers;
    static int c=0;
    void storeMem(ArrayList<String> val){
        String[] set = new String[100];
        String linec;
        int mc = 0;
        for(int i =1;i<textIndex(val);i++){
            if(val.get(i).charAt(val.get(i).length()-1)==':') {
                set = val.get(i + 1).split("[ ,]+");
                base.put(val.get(i).substring(0,val.get(i).length()-1),mc);
                for (int j = 1; j < set.length; j++) {
                    memory.add(Integer.parseInt(set[j]));
                    mc++;
                }
            }
        }
    }

    public void executer(){
        int counter = q.labels.get("main")+1;
        // System.out.println(counter);
        String  line;
        int i=0;
        try{
            while (counter < allLines.size()) {
                line = allLines.get(counter);
                int n=0;
                int flag=0;
                if(line.charAt(line.length()-1)==':')
                {
                    line = line.substring(0,line.length()-1);
                }
                else
                {
                   flag =-1;
                }
                String[] set = line.split("[ ,]+");
                if(flag==-1)
                {
                    n = opcodes.get(set[0].toUpperCase());
                    System.out.println(opcodes.get(set[0].toUpperCase()));
                }
//               for(int b=0;b<set.length;b++) {
//                   System.out.println(set[b]);
//               }

                if(q.labels.containsKey(set[0])){
                    counter++;
                    continue;
                }
                if(n == 0){
                    Registers[Integer.parseInt(set[1].substring(1))] = Registers[Integer.parseInt(set[2].substring(1))]+Registers[Integer.parseInt(set[3].substring(1))];
//                   System.out.println(Registers);
                    counter++;
                    continue;
                }
                if(n==9){
                    Registers[Integer.parseInt(set[1].substring(1))] = Integer.parseInt(set[2]);
//                  System.out.println(Registers[(int)set[1].charAt(1)]);
//                   System.out.println(Registers);
                    counter++;
                    continue;
                }
                if(n == 10){
                    Registers[Integer.parseInt(set[1].substring(1))] = Registers[Integer.parseInt(set[2].substring(1))] + Integer.parseInt(set[3]);
//                  System.out.println(Registers[(int)set[1].charAt(1)]);
//                   System.out.println(Registers);
                    counter++;
                    continue;
                }
                if(n == 3){
                    System.out.println("Check");
                    int index = (Registers[Integer.parseInt(set[2].substring(3,set[2].length()-1))] + set[2].charAt(0)-48);
                    memory.set(index,Registers[Integer.parseInt(set[1].substring(1))]);
                    System.out.println(memory);
                    counter++;
                    continue;
                }
                if(n == 1){
                    Registers[Integer.parseInt(set[1].substring(1))] = Registers[Integer.parseInt(set[2].substring(1))]-Registers[Integer.parseInt(set[3].substring(1))];
//                   System.out.println(Registers);
                    counter++;
                    continue;
                }
                if(n == 7){
                    counter = q.labels.get(set[1])+1;
//                   System.out.println(Registers);
                    continue;
                }
                if(n == 5){
                    if( Registers[Integer.parseInt(set[1].substring(1))]!= Registers[Integer.parseInt(set[2].substring(1))]){
                        counter = q.labels.get(set[3])+1;
                    }else
                        counter++;
                    continue;
                }
                if(n == 6){
                    if( Registers[Integer.parseInt(set[1].substring(1))]== Registers[Integer.parseInt(set[2].substring(1))]){
                        counter = q.labels.get(set[3])+1;
                    }else
                        counter++;
                    continue;
                }
                if(n == 11){
                    Registers[Integer.parseInt(set[1].substring(1))] = base.get(set[2]);
//                   System.out.println(Registers);
                    counter++;
                    continue;
                }
                if(n == 2){
                    int t = set[2].indexOf('(');
//                   System.out.println(memory.get(Registers[Integer.parseInt(set[2].substring(3,set[2].length()-1))] + Integer.parseInt(set[2].substring(0,t))));
                    Registers[Integer.parseInt(set[1].substring(1))] = memory.get(Registers[Integer.parseInt(set[2].substring(3,set[2].length()-1))] + Integer.parseInt(set[2].substring(0,t)));
                    // System.out.println(Registers[Integer.parseInt(set[1].substring(1))]);
//                   System.out.println(Integer.parseInt(set[2].substring(0,t)));
                    counter++;
                    continue;
                }
                if(n == 4){
                    if(Registers[Integer.parseInt(set[2].substring(1))]<Registers[Integer.parseInt(set[3].substring(1))]){
                        Registers[Integer.parseInt(set[1].substring(1))] = 1;
                    }
                    else
                    {
                        Registers[Integer.parseInt(set[1].substring(1))] = 0;
                    }
//                   Registers[(int)set[1].charAt(1) - 48] = memory.get((Registers[set[2].charAt(3)-48] + (int)set[2].charAt(0)-48));
//                   System.out.println(Registers);
                    counter++;
                    continue;
                }
                if(n == 8) {
                    if (Registers[19] == 10){
                        break;
                    }
//                   System.out.println(Registers);
                    counter++;
                    continue;
                }
            }
            for(int b=0;b<Registers.length;b++)
            {
                System.out.print(Registers[b]+" ");
            }
            System.out.println("");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(memory);
    }
//    final Lock queueLock = new ReentrantLock();
    Parser(BufferedReader file){
        Registers = new int[20];
        q = new PreParser(file);
        allLines = q.getList();
        Opcodes pt = new Opcodes();
        opcodes = pt.opt();
//        System.out.println(opcodes);
        this.storeMem(allLines);
//        System.out.println(memory);
//        System.out.println(base);
    }
    PreParser get(){
        return q;
    }
    int[] getReg(){
        return Registers;
    }
    ArrayList<Integer> getMem(){
        return memory;
    }
    int textIndex(ArrayList<String> values){
        for(int i=0;i<values.size();i++)
            if(values.get(i).equals(".text")){
                return i;
            }
        return -1;
    }

}


