import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Cache2 {
    static Cache2 cache2;
    int rear=-1;
    int front = -1;
    Memory m = Memory.getInstance();
    int[] cac2;
    String[] tag2;
    Cache2(){
        cac2 = new int[4096];
        tag2 = new String[512];
    }
    public static  synchronized Cache2 getInstance()
    {
        if(cache2==null)
        {
            cache2 = new Cache2();
        }
        return cache2;
    }
    public void push(String k,int num)
    {
        if(rear==-1 && front ==-1)
        {
            rear++;
            front++;
            tag2[rear] = k;
            for(int i=0;i<8;i++)
            {
                if(((num-num%8)+i)<m.getMem().size())
                {cac2[rear*8 +i] = m.getMem().get((num-num%8)+i);
                    System.out.print(cac2[rear * 8 + i] +" cache 2 " + (rear * 8 + i)+" ");}
            }
        }else
        {
            rear++;
            tag2[rear] = k;//thats it right?? wait just lemme check againok
            for(int i=0;i<8;i++)
            {
                if(((num-num%8)+i)<m.getMem().size())
                {cac2[rear*8 +i] = m.getMem().get((num-num%8)+i);
                    System.out.print(cac2[rear * 8 + i] +" cache 2 " + (rear * 8 + i)+" ");}
            }
        }
        System.out.println();
    }
    public void finalpush()
    {
        for(int i=front;i<=rear;i++)
        {
            if(tag2[i]!=null)
            {
                String l = tag2[i]+"000";
                for(int j=0;j<8;j++)
                {
                    if((parseInt(l,2)+j)<m.getMem().size())
                        m.getMem().set((parseInt(l,2)+j),cac2[8*i+j]);
                }
                // if(m.getmem().set((parseInt(l,2)))
            }
        }
    }
    public int pop(String num)
    {
        if(rear==-1 && front ==-1)
        {
           return 0;
        }
        else
            if(rear==front)
            {
                int k = cac2[8*rear];
               front=-1;
               rear = -1;
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
                        tag2 = l.toArray(new String[512]);
                        for(int k=0;k<8;k++)
                        {
                            for(int j = 8*i + k ; j <= (rear*8 +7);j++)
                            {
                                cac2[j] = cac2[j+1];
                                //System.out.print(cac2[j]);
                            }
//                            if(cac2==null)
//                                return -1;
//                            List<Integer> k1 = IntStream.of(cac2)
//                                    .boxed()
//                                    .collect(Collectors.toList());
//                            k1.remove(i*4 + k);
//                            cac2 = ArrayUtils.toPrimitive(k1.toArray(new Integer[4096]));
                        }
                        rear = rear-1;
                        return cac2[8*i];
                    }

                }

            }
            return 0;
    }
    public void set(String tag,int off,int newValue)
    {
               for(int i=front;i<=rear;i++)
               {
                   if(tag2[i]!=null)
                   if(tag2[i].equals(tag))
                   {
                       cac2[i*8+off] = newValue;
                       break;
                   }
               }
    }
    public void evict()
    {

            pop(tag2[front]);

    }
    public int search(String tag,int off,int ch) {
            int i;
            if(front==-1 && rear ==-1)
                return -1;
            else {
                for (i = front; i <= rear; i++) {
                    if (tag2[i].equals(tag)) {
                        int k = cac2[off + i * 8];
                        if(ch!=-1)
                        {int m = pop(tag);
                        push(tag, m);}
                        return k;
                    }
                }
                if (i == rear + 1)
                    return -1;
            }
        return 0;
    }
    public void insert(String tag,int num) {
        if (rear >= 512) {
            if (tag2[front] != null) {
                String iAdd = tag2[front] + "000";
                for (int i = 0; i < 8; i++) {
                    int k = cac2[front * 8 + i];
                    if (k != m.getMem().get(parseInt(iAdd, 2) + i)) {
                        m.getMem().set((parseInt(iAdd, 2) + i), k);

                    }
                }

            }
            evict();
        }
        push(tag, num);
    }
}
