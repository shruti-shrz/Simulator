import java.util.ArrayList;

public class Cache2 {
    static Cache2 cache2;
    ArrayList<Integer> cac2;
    Cache2(){
        cac2 = new ArrayList<Integer>();
    }
    public static  synchronized Cache2 getInstance()
    {
        if(cache2==null)
        {
            cache2 = new Cache2();
        }
        return cache2;
    }
    public ArrayList<Integer> getcac2(){
        return cac2;
    }
    public int getCache1Element(int index){
        return cac2.get(index);
    }
    public void insertcac2(int k)
    {
        cac2.add(k);
    }
}
