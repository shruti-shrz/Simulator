
import java.util.*;
import static java.lang.Integer.parseInt;
//import static jdk.nashorn.internal.objects.NativeArray.pop;

public class Cache2 {
    static Cache2 cache2;
    int rear=-1;
    int front = -1;
    Memory m = Memory.getInstance();
    int[] cac2;
    String[] tag2;
    Dictionary<Integer,List<Integer>> cache2_ref_table;
    Dictionary<Integer,List<Integer>> validator_ref_table;
    int sets;
    int block_size;
    int shift_size;
    int tag2_size;
    int cac2_size;
    Cache2(int size){
        cache2_ref_table = new Hashtable<>();
        validator_ref_table = new Hashtable<>();
        sets = 4;
        block_size = 100;
        shift_size = block_size / sets ;
        cac2_size = 1024*size;
        tag2_size = (1024*size)/shift_size;
        cac2 = new int[cac2_size];
        tag2 = new String[tag2_size];
        int initialiser = -1;

        for (int i=0;i<sets;i++){
            List<Integer> l = new ArrayList<Integer>();
            l.add(initialiser);
            l.add(initialiser);
            cache2_ref_table.put(i,l);
            validator_ref_table.put(i,l);
            initialiser += shift_size;
        }
    }
    public static  synchronized Cache2 getInstance(int size)
    {
        if(cache2==null)
        {
            cache2 = new Cache2(size);
        }
        return cache2;
    }
    public void push(String k, int num, int index)
    {
        int rear = cache2_ref_table.get(index).get(0);
        int front = cache2_ref_table.get(index).get(1);
        int rear_validator = validator_ref_table.get(index).get(0);
        int front_validator = validator_ref_table.get(index).get(0);
        if(rear == rear_validator && front == front_validator)
        {
            rear++;
            front++;
            tag2[rear] = k;
            for(int i=0;i<shift_size;i++)
            {
                if(((num-num% shift_size)+i)<m.getMem().size())
                {
                    cac2[rear*shift_size +i] = m.getMem().get((num-num%shift_size)+i);
                }
            }
            cache2_ref_table.get(index).set(0,rear);
            cache2_ref_table.get(index).set(1,front);
        }else
        {
            rear++;
            tag2[rear] = k;
            for(int i=0;i<shift_size;i++)
            {
                if(((num-num%shift_size)+i)<m.getMem().size())
                {
                    cac2[rear*shift_size +i] = m.getMem().get((num-num%shift_size)+i);
                }
            }
            cache2_ref_table.get(index).set(0,rear);
        }
    }
    public int pop(String num, int index)
    {
        int rear = cache2_ref_table.get(index).get(0);
        int front = cache2_ref_table.get(index).get(1);
        int rear_validator = validator_ref_table.get(index).get(0);
        int front_validator = validator_ref_table.get(index).get(0);
        if(rear==rear_validator && front ==front_validator)
        {
           return 0;
        }
        else
            if(rear==front)
            {
                int k = 8*rear;
                front= -1;
                rear = -1;
                cache2_ref_table.get(index).set(0,rear);
                cache2_ref_table.get(index).set(1,front);
                return k;
            }else
            {
                for(int i=front;i<=rear;i++)
                {
                    if(tag2[i]!=null)
                    if(tag2[i].equals(num))
                    {
                        List<String> l = new ArrayList<String>(Arrays.asList(tag2));
                        l.remove(num);
                        tag2 = l.toArray(new String[tag2_size]);
                        for(int k=0;k<shift_size;k++)
                        {
                            for(int j = shift_size*i + k ; j <= (rear*shift_size +shift_size-1);j++)
                            {
                                cac2[j] = cac2[j+1];
                            }
                        }
                        rear = rear--;
                        cache2_ref_table.get(index).set(0,rear);
                        return shift_size*i;
                    }

                }

            }
            return 0;
    }
    public void set(String add, int off,int newValue, int index)
    {
        int limiter = (int) (32 - (Math.log(sets)) - (Math.log(shift_size)));
        m.getMem().set(parseInt(add,2),newValue);
        int rear = cache2_ref_table.get(index).get(0);
        if(add.substring(0,limiter+1).equals(tag2[rear]))
        {
            cac2[rear*shift_size+off] = newValue;
        }

    }
    public void evict(int index)
    {
        int front = cache2_ref_table.get(index).get(1);
        pop(tag2[front],index);
    }
    public int search(String tag,int off,String add, int index) {
        int rear = cache2_ref_table.get(index).get(0);
        int front = cache2_ref_table.get(index).get(1);
        int rear_validator = validator_ref_table.get(index).get(0);
        int front_validator = validator_ref_table.get(index).get(0);
            int i;
            if(front==front_validator && rear ==rear_validator)
            {
                return -1;
            }
            else {
                for (i = front; i <= rear; i++) {
                    if (tag2[i].equals(tag)) {
                        int k = cac2[off + i * shift_size];
                        pop(tag,index);
                        push(tag, parseInt(add,2),index);
                        return cac2[rear*shift_size+ off];
                    }
                }
                if (i == rear + 1)
                {
                    return -1;
                }
            }
        return 0;
    }
    public void insert(String tag,int index ,int num) {
        int rear = cache2_ref_table.get(index).get(0);
        if (rear >= validator_ref_table.get(index).get(0)+1) {
            evict(index);
        }
        push(tag, num, index);
    }
}
