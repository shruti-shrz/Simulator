import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class ALU {
    int[] latch;
    Dictionary<String,Integer> opcodes;
    ArrayList<String> allLines;
    HashMap<String,Integer> base;
    Memory memory;
    static int counter;
    static ALU alu;
    HashMap<String,Integer> labels;
    public ALU(BufferedReader f,ArrayList<String> all,HashMap<String,Integer> b,HashMap<String,Integer> Labels){
        latch = new int[32];

//        q = new PreParser(f);
        allLines = all;
        //memory = Memory.getInstance();
        base = b;
        // System.out.println(labels);
        labels = Labels;
        counter = labels.get("main")+1;
    }


    public int executer(String[] arr,int[] Registers){
        //System.out.print(labels);
        for(int i=0;i<Registers.length;i++)
            latch[i] = Registers[i];
        // System.out.println(counter);
        String  line;
        int i=0;
//        for(int j=0;j<arr.length;j++){
//            System.out.print(arr[j]+" ");
//        }
        //System.out.println();
        try{
//            while (counter < allLines.size()) {
//                line = allLines.get(counter);
//                int n=0;
//                int flag=0;
//                if(line.charAt(line.length()-1)==':')
//                {
//                    line = line.substring(0,line.length()-1);
//                }
//                else
//                {
//                    flag =-1;
//                }

//                if(flag==-1)
//                {
//                    n = opcodes.get(set[0].toUpperCase());
//                    System.out.println(opcodes.get(set[0].toUpperCase()));
//                }
//               for(int b=0;b<set.length;b++) {
//                   System.out.println(set[b]);
//               }

//                if(q.labels.containsKey(set[0])){
//                    counter++;
//                    continue;
//                }
            if(Integer.parseInt(arr[0]) == 0){
                latch[Integer.parseInt(arr[1])] = latch[Integer.parseInt(arr[2])]+latch[Integer.parseInt(arr[3])];
//                   System.out.println(Registers);
                counter++;
                return latch[Integer.parseInt(arr[1])];// this is val, when is val = -1
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 1){
                latch[Integer.parseInt(arr[1])] = latch[Integer.parseInt(arr[2])]-latch[Integer.parseInt(arr[3])];
//                   System.out.println(Registers);
                counter++;
                return latch[Integer.parseInt(arr[1])];
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 2){
//                   System.out.println(memory.get(Registers[Integer.parseInt(set[2].substring(3,set[2].length()-1))] + Integer.parseInt(set[2].substring(0,t))));
                //   latch[Integer.parseInt(arr[1])] = memory.getMem().get(latch[Integer.parseInt(arr[2])] + Integer.parseInt(arr[3]));
                latch[Integer.parseInt(arr[1])] = latch[Integer.parseInt(arr[2])] + Integer.parseInt(arr[3]);
                // System.out.println(Registers[Integer.parseInt(set[1].substring(1))]);
//                   System.out.println(Integer.parseInt(set[2].substring(0,t)));
                counter++;
                return latch[Integer.parseInt(arr[1])];
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 3){
                int index = (latch[Integer.parseInt(arr[2])] + Integer.parseInt(arr[3]));
                //memory.getMem().set(index,latch[Integer.parseInt(arr[1])]);
                // System.out.println(memory);
                counter++;
                return index;
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 4){
                if(latch[Integer.parseInt(arr[2])]>latch[Integer.parseInt(arr[3])]){
                    latch[Integer.parseInt(arr[1])] = 1;
                }
                else
                {
                    latch[Integer.parseInt(arr[1])] = 0;
                }
//                   Registers[(int)set[1].charAt(1) - 48] = memory.get((Registers[set[2].charAt(3)-48] + (int)set[2].charAt(0)-48));
//                   System.out.println(Registers);
                counter++;
                return latch[Integer.parseInt(arr[1])];
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 5){
                if( latch[Integer.parseInt(arr[1])]!= latch[Integer.parseInt(arr[2])]){
                    counter = labels.get(arr[3])+1;
                }else
                    counter++;
                return -1;
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 6){
                if( latch[Integer.parseInt(arr[1])]== latch[Integer.parseInt(arr[2])]){
                    //System.out.print(labels.get(arr[3])+" gjhdgfh");
                    int t = labels.get(arr[3]);
                    counter = t+1;
                }else
                    counter++;
                return -1;
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 7){
                counter = labels.get(arr[1])+1;
                return -1;
//                   System.out.println(Registers);
//                    continue;
            }
            if(Integer.parseInt(arr[0])==9){
                latch[Integer.parseInt(arr[1])] = Integer.parseInt(arr[2]);
                counter++;
                return latch[Integer.parseInt(arr[1])];
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 10) {
                latch[Integer.parseInt(arr[1])] = latch[Integer.parseInt(arr[2])] + Integer.parseInt(arr[3]);
                counter++;
                return latch[Integer.parseInt(arr[1])];
//                    continue;
            }
            if(Integer.parseInt(arr[0]) == 11){
                //  System.out.println(base.get(arr[2]));
                latch[Integer.parseInt(arr[1])] = base.get(arr[2]);
//                   System.out.println(Registers);
                counter++;
                return latch[Integer.parseInt(arr[1])];
//                    continue;
            }


//                if(Integer.parseInt(arr[0]) == 8) {
//                    if (Registers[19] == 10){
//                        break;
//                    }
////                   System.out.println(Registers);
//                    counter++;
////                    continue;
//                }
//            }
//            for(int b=0;b<Registers.length;b++)
//            {
//                System.out.print(Registers[b]+" ");
//            }
//            System.out.println("");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
        //       System.out.println(memory);
    }

    int[] getReg(){
        return latch;
    }
    public static synchronized ALU getInstance(BufferedReader f,ArrayList<String> all,HashMap<String,Integer> b,HashMap<String,Integer> labels){
        if(alu == null)
        {
            alu = new ALU(f,all,b,labels);// okkk understood
        }
        return alu;
    }
}

