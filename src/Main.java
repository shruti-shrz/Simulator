import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    JFrame f;
    private JButton button1;
    Main(BufferedReader file){
        int offset = 360; // offset for frame setting
        f=new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Memory m = Memory.getInstance(); //

        Parser p0 = new Parser(file);
        JButton b = new JButton("Simulate");//creating instance of JButton
        b.setBounds(70+offset,400,90, 20);
        StringBuilder sb = new StringBuilder();
        for (Integer i : m.getMem()) {
            sb.append(i == null ? "" : i.toString()+", ");
        }
        JLabel l = new JLabel();
        l.setText(sb.toString());
        l.setBounds(130+offset,350,200, 40);

        JLabel l2 = new JLabel("Memory = ");
        l2.setBounds(70+offset,350,100, 40);

        JLabel l3 = new JLabel("Registers");
        l3.setBounds(50+offset,5,120, 20);

        JPanel panel = new JPanel();
        panel.setBounds(40+offset,30,30, 400);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panel2 = new JPanel();
        panel2.setBounds(100+offset,30,20, 400);
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        ArrayList<JLabel> lbw0 = new ArrayList<>();
        ArrayList<JLabel> lbw1 = new ArrayList<>();
        for(int i=0;i<=19;i++)
        {
            JLabel p = new JLabel();
            lbw0.add(p);
            lbw0.get(i).setText("t" + i + " = ");
            panel.add(lbw0.get(i));
        }
        for(int i=0;i<=19;i++)
        {
            JLabel p = new JLabel();
            lbw1.add(p);
            lbw1.get(i).setText(String.valueOf(p0.getalu().getReg()[i]));
            panel2.add(lbw1.get(i));
        }
        JTextArea tarea = p0.tarea; // this one
        tarea.setBounds(50,10,300,600);
        int startIndex = 0;
        int endIndex = 0;
        try {
            startIndex = tarea.getLineStartOffset(2);
            endIndex = tarea.getLineEndOffset(2);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);// for highlighting the line.
        try {
            tarea.getHighlighter().addHighlight(startIndex, endIndex, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        JLabel l5 = new JLabel("cycles = "); // for cycle label
        l5.setBounds(70+offset,430,100, 40);
        JLabel a = new JLabel();
        a.setText("0");
        a.setBounds(180+offset,430,200, 40);

        JLabel l6 = new JLabel("stalls = "); // for stall label
        l6.setBounds(70+offset,480,100, 40);
        JLabel c = new JLabel();
        c.setText("0");
        c.setBounds(180+offset,480,200, 40);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p0.startSimulation();
                System.out.println(p0.stall);
                a.setText(String.valueOf(p0.cycles));
                c.setText(String.valueOf(p0.stall));
                StringBuilder sb = new StringBuilder();
                for (Integer i : m.getMem()) {
                    sb.append(i == null ? "" : i.toString()+", ");
                }
                l.setText(sb.toString());
                for(int i=0;i<=19;i++) {
                    lbw1.get(i).setText(String.valueOf(p0.getReg()[i]));
                    panel2.add(lbw1.get(i));
                }
            }
        });

        f.add(b);//adding button in JFrame
        f.add(l2);
        f.add(l3);
        f.add(l);
        f.add(l5);
        f.add(l6);
        f.add(a);
        f.add(c);
        f.add(panel);
        f.add(panel2);
        f.add(tarea);
        f.setSize(400,500);//400 width and 500 height
        f.setLayout(null);
        //f.pack();//using no layout managers
        f.setVisible(true);
        //making the frame visible
    }

    public static void main(String[] args) {
        BufferedReader file;
        try {
//            String path1 = "C:/Users/visha/OneDrive/Desktop/ideas/themes.txt";
            String path2 = "C:/Users/Shruti priya/Downloads/bubblesort.asm";
            file = new BufferedReader(new FileReader(path2));
//                PreParser q = new PreParser(file);
//            Parser p = new Parser(file);

            new Main(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}