import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class Cache1 {
    static Cache1 cache1;
    int count1=0;
    int count2=0;
    int front=-1;
    int rear = -1;
    int front2=127;
    int rear2 = 127;
    Memory m = Memory.getInstance();
    Cache2 c2 = Cache2.getInstance();
    int[] cac1;
    String[] tag1;
    Cache1(){
        cac1 = new int[1024];//till 512 will be index 0 then index 1
        tag1 = new String[256];// till 128 will be index 0 then index 1
    }
    public static  synchronized Cache1 getInstance()
    {
        if(cache1==null)
        {
            cache1 = new Cache1();
        }
        return cache1;
    }
    public void push(String k,int num,int index)
    {
        if(index==0 && rear<128)
        if((rear==-1 && front ==-1))
        {
            rear++;
            front++;
            tag1[rear] = k;//index 0 no issue, index 1 other half will be filled
            for(int i=0;i<4;i++)
            {
                cac1[rear*4 +i] = m.getMem().get((num-num%4)+i);//same i did here
            }
        }else
        {
            rear++;
            tag1[rear] = k;//thats it right?? wait just lemme check againok
            for(int i=0;i<4;i++)
            {
                cac1[rear*4 +i] = m.getMem().get((num-num%4)+i);
            }
        }
        if(index==1&&rear2<256)
        {

            if((rear2==127 && front2 ==127))
            {
                rear2++;
                front2++;
                tag1[rear2] = k;//index 0 no issue, index 1 other half will be filled
                for(int i=0;i<4;i++)
                {
                    cac1[rear2*4 +i] = m.getMem().get((num-num%4)+i);//same i did here
                }
            }else
            {
                rear2++;
                tag1[rear2] = k;//thats it right?? wait just lemme check againok
                for(int i=0;i<4;i++)
                {
                    cac1[rear2*4 +i] = m.getMem().get((num-num%4)+i);
                }
            }
        }
    }
    public int pop(String num,int index) {
        if (index == 0 && rear<128)
        { if (rear == -1 && front == -1) {
                return 0;
            } else if (rear == front) {
                front = -1;
                rear = -1;
                return cac1[rear];
            } else {
                for (int i = front; i <= rear; i++) {
                    if (tag1[i].equals(num)) {
                        List<String> l = Arrays.asList(tag1);
                        l.remove(i);
//                        arrayList.stream()
//                                .mapToInt(Integer::intValue)
//                                .toArray();
                        tag1 = l.stream().toArray(String[]::new);
                        for (int k = 0; k < 4; k++) {
                            int finalK = k;
                            int finalI = i;
                            cac1 = IntStream.range(0, cac1.length)
                                    .filter(m -> m != finalI * 4 + finalK)
                                    .map(m -> cac1[m])
                                    .toArray();
                        }
                        rear--;
                        return cac1[i];
                    }

                }

            }
    }
        if(index==1 && rear2<256) {
            if (rear2 == 127 && front2 == 127) {
                return 0;
            } else if (rear2 == front2) {
                front2 = 127;
                rear2 = 127;
                return cac1[rear2+1];
            } else {
                for (int i = front2; i <= rear2; i++) {
                    if (tag1[i].equals(num)) {
                        List<String> l = Arrays.asList(tag1);
                        l.remove(i);
                        tag1 = l.stream().toArray(String[]::new);
                        for (int k = 0; k < 4; k++) {
                            int finalK = k;
                            int finalI = i;
                            cac1 = IntStream.range(0, cac1.length)
                                    .filter(m -> m != finalI * 4 + finalK)
                                    .map(m -> cac1[m])
                                    .toArray();
                        }
                        rear2--;
                        return cac1[i];
                    }

                }

            }
        }
        return 0;
    }
    public int search(String tag,int off,int index) {
        if (index == 0) {
            for (int i = 0; i < 128; i++) {
                if (tag1[i].equals(tag)) {
                    int k = cac1[off+i*4];
                    int m = pop(tag,index);
                    push(tag,m,index);
                    return k;
                }
            }
        } else if (index == 1) {
            for (int i = 128; i < 256;i++) {
                if (tag1[i].equals(tag)) {
                    int k = cac1[off+i*4];
                    int m = pop(tag,index);
                    push(tag,m,index);
                    return k;
                }
            }

        }
        return 0;
    }
    public void evict(int index)
    {
        if(index==0)
        pop(tag1[front],index);
        if(index==1)
        {
          pop(tag1[front2],index);
        }
    }
    public void insert(String tag,int index,int num)
        {
            push(tag,num,index);
        }

}
