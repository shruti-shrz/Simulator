import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;
public class Cache1 {
    static Cache1 cache1;
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
    public void pcache()
    {
        for(int i =front;i<=rear2;i++)
        {System.out.print(tag1[i] + "  ");
        }
        System.out.println("now tags");
        for(int i =front;i<=rear2;i++)
        {System.out.print(cac1[i] + "  ");
        }
    }
    public void finalPush()
    {
        if(front!=-1 && rear !=-1)
        for(int i=front;i<=rear;i++)
        {
            if(tag1[i]!=null)
            {
                String l = tag1[i]+"0"+"00";
                        for(int j=0;j<4;j++)
                        {
                            if(parseInt(l,2)+j < m.getMem().size())
                            { m.getMem().set((parseInt(l,2)+j),cac1[i*4 + j]);
                            System.out.println("heyy"+ cac1[i*4 + j]+(parseInt(l,2)+j));
                            }
                        }
               // if(m.getmem().set((parseInt(l,2)))
            }
        }
        if(front2!=-1 && rear2 !=-1)
        for(int i=front2;i<=rear2;i++)
        {
            if(tag1[i]!=null)
            {
                String l = tag1[i]+"1"+"00";
                for(int j=0;j<4;j++)
                {
                    if(parseInt(l,2)+j < m.getMem().size())
                    m.getMem().set(parseInt(l,2)+j,cac1[i*4 + j]);
                }
                // if(m.getmem().set((parseInt(l,2)))
            }
        }
    }
    public void push(String k,int num,int index)
    {
        if(index==0) {
            if ((rear == -1 && front == -1)) {
                rear++;
                front++;
                tag1[rear] = k;//index 0 no issue, index 1 other half will be filled
                for (int i = 0; i < 4; i++) {
                    if (((num - num % 4) + i) < m.getMem().size()) {
                        cac1[rear * 4 + i] = m.getMem().get((num - num % 4) + i);
                        System.out.print(cac1[rear * 4 + i] +" cache one index zero " + (rear * 4 + i)+" ");
                    }//same i did here
                }

            } else {
                rear++;
                tag1[rear] = k;//thats it right?? wait just lemme check againok
                for (int i = 0; i < 4; i++) {
                    if (((num - num % 4) + i) < m.getMem().size())
                        cac1[rear * 4 + i] = m.getMem().get((num - num % 4) + i);
                    System.out.print(cac1[rear * 4 + i] +" cache one index zero " + (rear * 4 + i)+" ");
                }
            }
            System.out.println();
        }
        if(index==1)
        {

            if((rear2==127 && front2 ==127))
            {
                rear2++;
                front2++;
                tag1[rear2] = k;//index 0 no issue, index 1 other half will be filled
                for(int i=0;i<4;i++)
                {
                    if(((num-num%4)+i)<m.getMem().size())
                    { cac1[rear2*4 +i] = m.getMem().get((num-num%4)+i);
                    System.out.print(cac1[rear2 * 4 + i] +" cache 1 index 1 " + (rear2 * 4 + i)+" ");}//same i did here}
                }
            }else
            {
                rear2++;
                tag1[rear2] = k;
                //System.out.print(tag1.length + " ");
                for(int i=0;i<4;i++)
                {
                    if(((num-num%4)+i)<m.getMem().size())
                    {
                        //System.out.print(cac1.length + "  l ");
                        cac1[rear2*4 +i] = m.getMem().get((num-num%4)+i);
                        System.out.print(cac1[rear * 4 + i] +" cache 1 index 0 " + (rear * 4 + i)+" ");
                    }
                }
            }
        }
        System.out.println();
    }
    public int pop(String num,int index) {
        if (index == 0)
        { if (rear == -1 && front == -1) {
                return -1;
            } else if (rear == front) {
                int k = cac1[4*rear];
                front = -1;
                rear = -1;
                return k;
            } else {
                for (int i = front; i <= rear; i++) {
                    if(tag1[i]!=null)
                    if (tag1[i].equals(num)) {
                        List<String> l = new ArrayList<String>(Arrays.asList(tag1));
                        l.remove(num);
                        tag1 = l.toArray(new String[256]);
                       // tag1 = l.stream().toArray(String[]::new);
                        for (int k = 0; k < 4; k++) {
//                            int finalK = k;
//                            int finalI = i;
//                            cac1 = IntStream.range(0, cac1.length)
//                                    .filter(m -> m != finalI * 4 + finalK)
//                                    .map(m -> cac1[m])
//                                    .toArray();
                            if(cac1==null)
                                return -1;
                            for(int j = 4*i + k ; j <= (rear*4 +3);j++)
                            {
                                cac1[j] = cac1[j+1];
                                //System.out.print(cac1[j]);
                            }
                        }
                        rear--;
                        return cac1[4*i];
                    }

                }

            }
    }
        if(index==1) {
            if (rear2 == 127 && front2 == 127) {
                return 0;
            } else if (rear2 == front2) {
                int k = cac1[4*front2];
                front2 = 127;
                rear2 = 127;
                return k;
            } else {
                for (int i = front2; i <= rear2; i++) {
                    if(tag1[i]!=null)
                    if (tag1[i].equals(num)) {
                        List<String> l = new ArrayList<String>(Arrays.asList(tag1));
                        l.remove(num);
                        tag1 = l.toArray(new String[256]);
                        for (int k = 0; k < 4; k++) {
                            if(cac1==null)
                                return -1;
                            for(int j = 4*i + k ; j <= (rear2*4 +3);j++)
                            {
                                cac1[j] = cac1[j+1];
                                //System.out.print(cac1[j]);
                            }
                        }
                        rear2--;
                        return cac1[4*i];
                    }

                }

            }
        }
        return 0;
    }
    public int search(String tag,int off,int index) {
        int i=0;
        if (index == 0) {
            if(rear==-1 && front==-1)
            {
               return -1;
            }else {
                for (i = front; i <= rear; i++) {
                    if(tag1[i]!=null)
                    if (tag1[i].equals(tag)) {
                        int k = cac1[off + i * 4];
                        int m = pop(tag, index);
                        push(tag, m, index);
                        return k;
                    }
                }
                if (i == rear + 1)//this for not find
                {
                    return -1;
                }
            }
        } else if (index == 1) {
            if (rear2 == 127 && front2 == 127) {
                return -1;
            } else {
                for (i = front2; i <= rear2; i++) {
                   // System.out.println(tag1[i]);
                    if(tag1[i]!=null)
                    if (tag1[i].equals(tag)) {
                        int k = cac1[off + i * 4];
                        int m = pop(tag, index);
                        push(tag, m, index);
                        return k;
                    }
                }
                if (i == rear2 + 1)//this for not find
                {
                    return -1;
                }
            }
        }
        return 0;
    }
    public void set(String tag,int off,int index,int newValue,String add) {
        int i;
        if (index == 0) {
//            for ( i = front; i <= rear; i++) {
//                if(tag1[i]!=null)
//                if (tag1[i].equals(tag)) {
//                    cac1[off+i*4] = newValue;
//                    break;
//                }
//            }
            cac1[rear*4 + off] = newValue;

        } else if (index == 1) {
//            for (i = front2; i <= rear2;i++) {
//                if(tag1[i]!=null)
//                if (tag1[i].equals(tag)) {
//                    cac1[off+i*4] = newValue;
//                    break;
//                }
//
//            }
            cac1[rear2*4 +off] = newValue;
        }
        int l = c2.search(add.substring(0,30),parseInt(add.substring(30),2),-1);
        if(l==-1)
        {
            m.getMem().set(parseInt(add,2),newValue);
        }
        else
        {
            m.getMem().set(parseInt(add,2),newValue);
            c2.set(add.substring(0,30),parseInt(add.substring(30)),newValue);
        }
       // writethrough(tag,int off,)
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
            if(index==0 && rear >=128)
            { evict(index);}
            else
                if(index==1 && rear2>=256)
                {
                    evict(index);
                }
            push(tag,num,index);
        }

}
