package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Visualization {
    PainterThread painterThread;
    public Visualization() {
        JFrame jFrame = new StartJFrame();
        jFrame.setSize(920,750);

        jFrame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }
            @Override
            public void mousePressed(MouseEvent e) {
                painterThread = new PainterThread(jFrame);
                painterThread.start();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                painterThread.running = false;
            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(painterThread != null) {
                    painterThread.running = false;
                }
                System.exit(0);
            }
        });

        jFrame.setVisible(true);
    }
    static class StartJFrame extends JFrame {
        public void paint(Graphics g) {
            //Størrelsesforhold 1:5
            //Bander
            g.drawLine(10,35,10, 315);
            g.drawLine(10,355,10,635);
            g.drawLine(10,35,910,35);
            g.drawLine(10,635,910,635);
            g.drawLine(910,35,910,285);
            g.drawLine(910,385,910,635);

            //<g.drawLine(460,35,460,335);

            //Dims i midten TODO regn den om så den passer(den starter fra x, den skal starte i midten af x)
            g.fillRect(460,270, 20,100);
            g.fillRect(420,310, 100,20);

            //tennisbold
            g.drawOval(55,130,20,20);

        }
    }


    public static class PainterComponent extends JComponent
    {
        int x, y;

        PainterComponent(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            Color c = new Color(1.0f,0.0F,0.0F);
            g.setColor(c);

            g.fillOval(x-2, y-2, 4, 4);
        }
    }
    static class PainterThread extends Thread{
        JFrame jFrame;
        boolean running = true;
        PainterThread(JFrame jFrame){
            this.jFrame = jFrame;
        }
        @Override
        public void run(){
            int lastX=-1,lastY=-1;
            while(running) {
                PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                int tempX, tempY;

                tempX = pointerInfo.getLocation().x - jFrame.getX();
                tempY = pointerInfo.getLocation().y - jFrame.getY()-30;
                    if (!(lastX == tempX && lastY == tempY)) {
                        try {
                            PainterComponent painterComponent = new PainterComponent(tempX, tempY);
                            jFrame.add(painterComponent);
                            jFrame.revalidate();
                            jFrame.repaint();
                            lastX = tempX;
                            lastY = tempY;
                            sleep(5);
                        } catch(InterruptedException e){
                            throw new RuntimeException(e);
                        }
                }
            }
            this.interrupt();
        }
    }
}
