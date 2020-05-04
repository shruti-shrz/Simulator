import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

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
                cac2[rear*8 +i] = m.getMem().get((num-num%8)+i);
            }
        }else
        {
            rear++;
            tag2[rear] = k;//thats it right?? wait just lemme check againok
            for(int i=0;i<8;i++)
            {
                cac2[rear*8 +i] = m.getMem().get((num-num%8)+i);
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
               front=-1;
               rear = -1;
            }else
            {
                for(int i=front;i<=rear;i++)
                {
                    if(tag2[i].equals(num))
                    {
                        List<String> l = Arrays.asList(tag2);
                        l.remove(i);
//                        arrayList.stream()
//                                .mapToInt(Integer::intValue)
//                                .toArray();
                        tag2 = l.stream().toArray(String[]::new);
                        for(int k=0;k<8;k++)
                        {
                            int finalK = k;
                            int finalI = i;
                            cac2 = IntStream.range(0, cac2.length)
                                    .filter(m -> m != finalI *8 + finalK)
                                    .map(m -> cac2[m])
                                    .toArray();
                        }
                        rear = rear-1;
                        return cac2[i];
                    }

                }

            }
            return 0;
    }
    public int search(String tag,int off) {

            for (int i = 0; i < 512; i++) {
                if (tag2[i].equals(tag)) {
                    int k = cac2[off+i*4];
                    int m = pop(tag);
                    push(tag,m);
                    return k;
                }
            }

        return 0;
    }
    public void insert(String tag,int num)
    {
        if(rear<512)
        {
            push(tag,num);
        }
    }
}
