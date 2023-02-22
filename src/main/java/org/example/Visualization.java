package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Visualization {
    public static void main(String[] args) {
        JFrame jFrame = new StartJFrame();
        jFrame.setSize(920,750);

        jFrame.addMouseListener(new MouseListener() {
            PainterThread painterThread;
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
                painterThread.interrupt();
            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

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

            g.drawLine(460,35,460,335);

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

            Color c = new Color(0.25F,0.0F,1.0F);
            g.setColor(c);

            g.fillOval(x-2, y-2, 4, 4);
        }
    }
    static class PainterThread extends Thread{
        JFrame jFrame;
        PainterThread(JFrame jFrame){
            this.jFrame = jFrame;
        }
        @Override
        public void run(){
            int lastX=-1,lastY=-1;
            while(true) {
                PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                    if (!(lastX == pointerInfo.getLocation().x && lastY == pointerInfo.getLocation().y)) {
                        try {
                        PainterComponent painterComponent = new PainterComponent(pointerInfo.getLocation().x, pointerInfo.getLocation().y);
                        jFrame.add(painterComponent);
                        jFrame.revalidate();
                        jFrame.repaint();
                        lastX = pointerInfo.getLocation().x;
                        lastY = pointerInfo.getLocation().y;
                        sleep(20);
                    } catch(InterruptedException e){
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
