
import java.util.*;
import static java.lang.Integer.parseInt;
//import static jdk.nashorn.internal.objects.NativeArray.pop;

public class Cache2 {
    static Cache2 cache2;
    Memory m = Memory.getInstance();
    int[] cac2;
    String[] tag2;
    Dictionary<Integer,List<Integer>> cache2_ref_table;
    int sets;
    int shift_size;
    int tag2_size;
    int cac2_size;
    ArrayList<Integer>  initials = new ArrayList<>();
    Cache2(Dictionary<String,ArrayList<Integer>> des){
        cache2_ref_table = new Hashtable<>();
        sets =des.get("cache2").get(1);
        shift_size = des.get("cache2").get(0)/(des.get("cache2").get(1)*des.get("cache2").get(2));
        cac2_size = des.get("cache2").get(0);
        tag2_size = cac2_size/des.get("cache2").get(1);
        cac2 = new int[des.get("cache2").get(0)];
        tag2 = new String[des.get("cache2").get(0)/des.get("cache2").get(1)];
        int initialiser = -1;

        for (int i=0;i<des.get("cache2").get(2);i++){
            List<Integer> l = new ArrayList<Integer>();
            l.add(initialiser);
            l.add(initialiser);
            initials.add(initialiser);
            initialiser += shift_size;
            l.add(initialiser+1);
            cache2_ref_table.put(i,l);
        }
       // System.out.println(cache2_ref_table);
        //System.out.println(initials);
    }
    public static  synchronized Cache2 getInstance(Dictionary<String,ArrayList<Integer>> des)
    {
        if(cache2==null)
        {
            cache2 = new Cache2(des);
        }
        return cache2;
    }
    public void push(String k, int num, int index)
    {
       // System.out.println("check");
        int rear = cache2_ref_table.get(index).get(0);
        int front = cache2_ref_table.get(index).get(1);
            int rear_validator = initials.get(index);
            int front_validator = initials.get(index);
           // System.out.println("push1"+rear+" "+front+" "+rear_validator+" "+front_validator);
            if (rear == rear_validator && front == front_validator) {
                rear++;
                front++;
                tag2[rear] = k;
                for (int i = 0; i < sets; i++) {
                    if (((num - num % sets) + i) < m.getMem().size()) {
                        cac2[rear * sets + i] = m.getMem().get((num - num % sets) + i);
                    }
                }
                cache2_ref_table.get(index).set(0, rear);
                cache2_ref_table.get(index).set(1, front);
            } else {
                rear++;
                tag2[rear] = k;
                for (int i = 0; i < sets; i++) {
                    if (((num - num % sets) + i) < m.getMem().size()) {
                        cac2[rear * sets + i] = m.getMem().get((num - num % sets) + i);
                    }
                }
                cache2_ref_table.get(index).set(0, rear);
            }
        }

    public int pop(String num, int index)
    {
        if(cache2_ref_table.get(index)!=null) {
            int rear = cache2_ref_table.get(index).get(0);
            int front = cache2_ref_table.get(index).get(1);
            int rear_validator = initials.get(index);
            int front_validator = initials.get(index);
            if (rear == rear_validator && front == front_validator) {
                return 0;
            } else if (rear == front) {
                int k = sets * rear;
                front = front_validator;
                rear = rear_validator;
                cache2_ref_table.get(index).set(0, rear);
                cache2_ref_table.get(index).set(1, front);
                return k;
            } else {
                for (int i = front; i <= rear; i++) {
                    if (tag2[i] != null)
                        if (tag2[i].equals(num)) {
                            List<String> l = new ArrayList<String>(Arrays.asList(tag2));
                            l.remove(num);
                            tag2 = l.toArray(new String[tag2_size]);
                            for (int k = 0; k < sets; k++) {
                                for (int j = sets * i + k; j <= (rear * sets + sets - 1); j++) {
                                    cac2[j] = cac2[j + 1];
                                }
                            }
                            rear--;
                            cache2_ref_table.get(index).set(0, rear);
                            return sets * i;
                        }

                }

            }
        }
            return 0;
    }
    public void set(String add,int tag_bit, int off,int newValue, int index)
    {

            m.getMem().set(parseInt(add,2),newValue);
            int rear = cache2_ref_table.get(index).get(0);
            if (add.substring(0, tag_bit).equals(tag2[rear])) {
                cac2[rear * sets + off] = newValue;
            }


    }
    public void evict(int index)
    {
        int front = cache2_ref_table.get(index).get(1);
        pop(tag2[front],index);
    }
    public int search(String tag,int off,String add, int index) {
        if(cache2_ref_table.get(index)!=null) {
            int rear = cache2_ref_table.get(index).get(0);
            int front = cache2_ref_table.get(index).get(1);
            int rear_validator = initials.get(index);
            int front_validator = initials.get(index);
           // System.out.println(rear+"  ch "+front);
            int i;
            if (front == front_validator && rear == rear_validator) {
                return -1;
            } else {
                for (i = front; i <= rear; i++) {
                    if(tag2[i]!=null)
                    if (tag2[i].equals(tag)) {
                        pop(tag, index);
                        push(tag, parseInt(add, 2), index);
                       // System.out.println("yes");
                        return cac2[rear * sets + off];
                    }
                }
                if (i == rear + 1) {
                   // System.out.println("no");
                    return -1;
                }
            }
        }
        return 0;
    }
    public void insert(String tag,int index ,int num) {
        if(cache2_ref_table.get(index)!=null) {
            int rear = cache2_ref_table.get(index).get(0);
            if (rear >= cache2_ref_table.get(index).get(2)) {
                evict(index);
            }
        }
        push(tag, num, index);
    }
}
