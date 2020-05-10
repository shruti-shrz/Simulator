import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
    JFrame f;
    private JButton button1;
    ALU a;
    Registers r;

    Main(BufferedReader file){
        int offset = 600;
        f=new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Memory m = Memory.getInstance();
        r = Registers.getInstance();
        Parser p0 = new Parser(file);
        JButton b = new JButton("Simulate");
        JButton b2 = new JButton("Step by Step");
        b.setBounds(70+offset,560,90, 20);
        b2.setBounds(70+offset,590,120,20);
        StringBuilder sb = new StringBuilder();
        for (Integer i : m.getMem()) {
            sb.append(i == null ? "" : i.toString()+", ");
        }
        JLabel l = new JLabel();
        l.setText(sb.toString());
        l.setBounds(130+offset,350,500, 40);

        JLabel l2 = new JLabel("Memory = ");
        l2.setBounds(70+offset,350,200, 40);

        JLabel l3 = new JLabel("Registers");
        l3.setBounds(70+offset,5,120, 20);

        JPanel panel = new JPanel();
        panel.setBounds(70+offset,30,50, 400);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panel2 = new JPanel();
        panel2.setBounds(120+offset,30,30, 400);
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        ArrayList<JLabel> lbw0 = new ArrayList<>();
        ArrayList<JLabel> lbw1 = new ArrayList<>();
        for(int i=0;i<=19;i++)
        {
            JLabel p = new JLabel();
            lbw0.add(p);
            lbw0.get(i).setText("t" + i + "  =  ");
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
        tarea.setBounds(70,10,550,600);
        JLabel l6 = new JLabel("stalls = "); // for stall label
        l6.setBounds(70+offset,410,100, 40);
        JLabel c = new JLabel();
        c.setText("0");
        c.setBounds(200+offset,410,200, 40);

        JLabel l5 = new JLabel("cycles = "); // for cycle label
        l5.setBounds(70+offset,380,100, 40);
        JLabel a = new JLabel();
        a.setText("0");
        a.setBounds(200+offset,380,200, 40);

        JLabel l7 = new JLabel("Cache 1 miss rate = ");
        l7.setBounds(70+offset,440,120,40);
        JLabel d = new JLabel();
        d.setText("0");
        d.setBounds(200+offset,440,200, 40);

        JLabel l8 = new JLabel("Cache 2 miss rate = ");
        l8.setBounds(70+offset,470,120,40);
        JLabel h = new JLabel();
        h.setText("0");
        h.setBounds(200+offset,470,200, 40);

        JLabel l9 = new JLabel("IPC = ");
        l9.setBounds(70+offset,500,120,40);
        JLabel g = new JLabel();
        g.setText("0");
        g.setBounds(200+offset,500,200, 40);

        final int[] startIndex = {0};
        final int[] endIndex = {0};

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int l1,m1;
                System.out.println(startIndex[0]+" "+endIndex[0]);
                if(p0.currInstr[0] == "7")
                    p0.length = p0.getalu().labels.get(p0.currInstr[1])+1;
                else
                    if(p0.currInstr[0]=="5")
                    {
                        l1 = r.getreg(Integer.parseInt(p0.currInstr[1]));
                        m1 = r.getreg(Integer.parseInt(p0.currInstr[2]));
                        if( l1!= m1){
                            p0.length = p0.getalu().labels.get(p0.currInstr[3])+1;
                        }else
                            p0.length = p0.getalu().counter +1;;
                    }
                    else
                        if(p0.currInstr[0]=="6")
                        {
                            l1 = r.getreg(Integer.parseInt(p0.currInstr[1]));
                            m1 = r.getreg(Integer.parseInt(p0.currInstr[2]));
                            if( l1== m1){
                                p0.length = p0.getalu().labels.get(p0.currInstr[3])+1;
                            }else
                                p0.length = p0.getalu().counter +1;;
                        }
                        if(p0.currInstr[0]=="3")
                        {
                            p0.length = p0.getalu().counter + 1;
                        }
                    else
                    p0.length = p0.getalu().counter +1;

                Highlighter highlighter = tarea.getHighlighter();
                highlighter.removeAllHighlights();
                try {
                    startIndex[0] = tarea.getLineStartOffset(p0.alu.counter);
                    endIndex[0] = tarea.getLineEndOffset(p0.alu.counter);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                Highlighter.HighlightPainter curr_painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                try {
                    tarea.getHighlighter().addHighlight(startIndex[0], endIndex[0], curr_painter);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                if(p0.length<p0.allLines.size())
                {
                    p0.startSimulation(0);
                }

                a.setText(String.valueOf(p0.cycle2));
                c.setText(String.valueOf(p0.stall));
                d.setText(String.valueOf((double)Math.round((p0.miss_rate_1)*10000)/10000));
                h.setText(String.valueOf((double)Math.round((p0.miss_rate_2)*10000)/10000));
                g.setText(String.valueOf((double)Math.round((p0.ipc)*10000)/10000));
                StringBuilder sb = new StringBuilder();
                for (Integer i : m.getMem()) {
                    sb.append(i == null ? "" : i.toString()+", ");
                }
                l.setText(sb.toString());
                for(int i=0;i<=19;i++) {
                    lbw1.get(i).setText(String.valueOf(r.getreg(i)));
                    panel2.add(lbw1.get(i));
                }
            }
        });
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p0.length = p0.allLines.size();
                p0.startSimulation(-1);
                a.setText(String.valueOf(p0.cycles));
                c.setText(String.valueOf(p0.stall));
                d.setText(String.valueOf((double)Math.round((p0.miss_rate_1)*10000)/10000));
                h.setText(String.valueOf((double)Math.round((p0.miss_rate_2)*10000)/10000));
                g.setText(String.valueOf((double)Math.round((p0.ipc)*10000)/10000));
                StringBuilder sb = new StringBuilder();
                for (Integer i : m.getMem()) {
                    sb.append(i == null ? "" : i.toString()+", ");
                }
                l.setText(sb.toString());
                for(int i=0;i<=19;i++) {
                    lbw1.get(i).setText(String.valueOf(r.getreg(i)));
                    panel2.add(lbw1.get(i));
                }
            }
        });

        f.add(b);
        f.add(b2);
        f.add(l2);
        f.add(l3);
        f.add(l);
        f.add(l5);
        f.add(l6);
        f.add(l7);
        f.add(l8);
        f.add(l9);
        f.add(a);
        f.add(c);
        f.add(d);
        f.add(h);
        f.add(g);
        f.add(panel);
        f.add(panel2);
        f.add(tarea);
        f.setSize(1000,800);
        f.setLayout(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        BufferedReader file;
        try {
            String path2 = "./bubblesort.asm";
            file = new BufferedReader(new FileReader(path2));
            new Main(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}