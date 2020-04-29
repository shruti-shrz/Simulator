import java.util.ArrayList;

public class Cache1 {
    static Cache1 cache1;
    ArrayList<Integer> cac1;
    Cache1(){
        cac1 = new ArrayList<Integer>();
    }
    public static  synchronized Cache1 getInstance()
    {
        if(cache1==null)
        {
            cache1 = new Cache1();
        }
        return cache1;
    }
    public ArrayList<Integer> getcac1(){
        return cac1;
    }
    public void insertcac1(int k)
    {
        cac1.add(k);
    }
}
