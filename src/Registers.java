
public class Registers {
    static Registers registers;
    int[] reg;
    Registers(){
        reg = new int[32];
    }
    public static  synchronized Registers getInstance()
    {
        if(registers==null)
        {
            registers = new Registers();
        }
        return registers;
    }
    public int getreg(int index){
        return reg[index];
    }
    public void printreg()
    {
        for(int i=0;i<32;i++)
            System.out.print(reg[i]+" ");
    }
    public void insert(int index,int k)
    {
        reg[index] = k;
    }
    public int[] getC(){
        return reg;
    }
}
