import java.util.*;

import static java.lang.Integer.parseInt;
public class Cache1 {
    int[] cac1;
    String[] tag1;
    static Cache1 cache1;
    Memory m = Memory.getInstance();
    Cache2 c2;
    Dictionary<Integer,ArrayList<Integer>> cache1_ref_table = new Hashtable<>();
    ArrayList<Integer>  initials = new ArrayList<>();
    int offset ;

    Cache1(Dictionary<String,ArrayList<Integer>> des) {
        cac1 = new int[des.get("cache1").get(0)];
        tag1 = new String[des.get("cache1").get(0)/des.get("cache1").get(1)];
        c2 = Cache2.getInstance(des);
        int shift_size = des.get("cache1").get(0)/(des.get("cache1").get(1)*des.get("cache1").get(2));//
        int initialiser = -1;
        offset = des.get("cache1").get(1);
        for (int i=0;i<des.get("cache1").get(2);i++){//same thing do for cache 2
            ArrayList<Integer> l = new ArrayList<>();
            l.add(initialiser);
            l.add(initialiser);
            initials.add(initialiser);
            initialiser += shift_size;
            l.add(initialiser+1);
            cache1_ref_table.put(i,l);

        }
    }

    public static synchronized Cache1 getInstance(Dictionary<String,ArrayList<Integer>> des) {
        if (cache1 == null) {
            cache1 = new Cache1(des);
        }
        return cache1;
    }

//    public void finalPush() {
//        if (front != -1 && rear != -1)
//            for (int i = front; i <= rear; i++) {
//                if (tag1[i] != null) {
//                    String l = tag1[i] + "0" + "00";
//                    for (int j = 0; j < 4; j++) {
//                        if (parseInt(l, 2) + j < m.getMem().size()) {
//                            if(cac1[i * 4 + j]!=0)
//                            m.getMem().set((parseInt(l, 2) + j), cac1[i * 4 + j]);
//                        }
//                    }
//                }
//            }
//        if (front2 != -1 && rear2 != -1)
//            for (int i = front2; i <= rear2; i++) {
//                if (tag1[i] != null) {
//                    String l = tag1[i] + "1" + "00";
//                    for (int j = 0; j < 4; j++) {
//                        if (parseInt(l, 2) + j < m.getMem().size())
//                            if(cac1[i * 4 + j]!=0)
//                            m.getMem().set(parseInt(l, 2) + j, cac1[i * 4 + j]);
//                    }
//                }
//            }
//    }

    public void push(String k, int num, int index) {
        if(cache1_ref_table.get(index)!=null)
        {
            if(cache1_ref_table.get(index).get(0)==initials.get(index) && cache1_ref_table.get(index).get(1)==initials.get(index))
            {
              int t =  cache1_ref_table.get(index).get(0);
              t = t+1;
              int t2 =  cache1_ref_table.get(index).get(1);
              t2 = t2+1;
              cache1_ref_table.get(index).add(0,t);
                cache1_ref_table.get(index).add(1,t2);
                tag1[t2] = k;
                for (int i = 0; i < offset; i++) {
                    if (((num - num % offset) + i) < m.getMem().size()) {
                        cac1[t2 * offset + i] = m.getMem().get((num - num % offset) + i);
                    }
                }
            }
            else {
                int t2 =  cache1_ref_table.get(index).get(1);
                t2 = t2+1;
                cache1_ref_table.get(index).add(1,t2);
            tag1[t2] = k;
            for (int i = 0; i < offset; i++) {
                if (((num - num % offset) + i) < m.getMem().size())
                {  cac1[t2 * offset + i] = m.getMem().get((num - num % offset) + i);
                   // System.out.print("cache one "+ cac1[rear*4 + i]+ " "+(rear*4 +i));
                    }
            }
        }
        }
    }

    public int pop(String num,int index) {
        if(cache1_ref_table.get(index)!=null)
        {
            if(cache1_ref_table.get(index).get(0)==initials.get(index) && cache1_ref_table.get(index).get(1)==initials.get(index))
                return -1;
            else
                if(cache1_ref_table.get(index).get(0)==cache1_ref_table.get(index).get(1))
                {
                    int t2 =  cache1_ref_table.get(index).get(1);
                    int k = offset*t2;
                    cache1_ref_table.get(index).add(0,initials.get(index));
                    cache1_ref_table.get(index).add(1,initials.get(index));
                    return k;
                }
                else
                {
                    int t =  cache1_ref_table.get(index).get(0);
                    int t2 =  cache1_ref_table.get(index).get(1);
                    for (int i = t; i <= t2; i++) {
                        if(tag1[i]!=null)
                            if (tag1[i].equals(num)) {
                                List<String> l = new ArrayList<String>(Arrays.asList(tag1));
                                l.remove(num);
                                tag1 = l.toArray(new String[256]);
                                for (int k = 0; k < offset; k++) {
                                    if(cac1==null)
                                        return -1;
                                    for(int j = offset*i + k ; j <= (t2*offset +offset-1);j++)
                                    {
                                        cac1[j] = cac1[j+1];
                                    }
                                }
                                t2 = t2-1;
                                cache1_ref_table.get(index).add(1,t2);
                                return 4*i;
                            }

                    }
                }
        }

        return 0;
    }
    public int search(String tag,int off,int index,String add) {
        int i;
        if(cache1_ref_table.get(index)!=null)
        {
            if(cache1_ref_table.get(index).get(0)==initials.get(index) && cache1_ref_table.get(index).get(1)==initials.get(index))
                return -1;
            else
            {
                int t =  cache1_ref_table.get(index).get(0);
                int t2 =  cache1_ref_table.get(index).get(1);
                for (i = t; i <= t2; i++) {
                    if(tag1[i]!=null)
                        if (tag1[i].equals(tag)) {
                            pop(tag, index);
                            push(tag, parseInt(add,2), index);
                            return cac1[t2*offset +off];
                        }
                }
                if (i == t2 + 1)
                {
                    return -1;
                }
            }
        }
        return 0;
    }
    public void set(int tag_bit_c2,int index_bit_c2,int off,int index,int newValue,String add) {
        if(cache1_ref_table.get(index)!=null)
        {
            int t2 =  cache1_ref_table.get(index).get(1);
            cac1[t2*offset+off] = newValue;
        }
        int l;
        if(index_bit_c2==0)
         l = c2.search(add.substring(0,tag_bit_c2),parseInt(add.substring(tag_bit_c2+index_bit_c2),2),add,0);
        else
            l = c2.search(add.substring(0,tag_bit_c2),parseInt(add.substring(tag_bit_c2+index_bit_c2),2),add,parseInt(add.substring(tag_bit_c2,(index_bit_c2+tag_bit_c2)),2));
        if(l==-1)
        {
            m.getMem().set(parseInt(add,2),newValue);
        }
        else
        {
            if(index_bit_c2==0)
            c2.set(add, tag_bit_c2,parseInt(add.substring(tag_bit_c2+index_bit_c2),2),newValue,0);
            else
                c2.set(add,tag_bit_c2,parseInt(add.substring(tag_bit_c2+index_bit_c2),2),newValue,parseInt(add.substring(tag_bit_c2,(tag_bit_c2+index_bit_c2)),2));
        }
    }
    public void evict(int index)
    {
        if(cache1_ref_table.get(index)!=null)
        {
            int t =  cache1_ref_table.get(index).get(0);
            pop(tag1[t],index);
        }
    }
    public void insert(String tag,int index,int num)
        {
            if(cache1_ref_table.get(index)!=null)
            {
               if(cache1_ref_table.get(index).get(1) >= cache1_ref_table.get(index).get(2))
               {
                  evict(index);
               }

            }
//            if(index==0 && rear >=128)
//            { evict(index);}
//            else
//                if(index==1 && rear2>=256)
//                {
//                    evict(index);
//                }
            push(tag,num,index);
        }

}
