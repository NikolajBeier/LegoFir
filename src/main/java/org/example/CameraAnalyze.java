package org.example;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;

public class CameraAnalyze {
    JFrame jFrame = new JFrame();
    JPanel jPanel = new JPanel();

    public CameraAnalyze(){
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        jFrame.setSize(920,720);
        webcam.setViewSize(webcam.getViewSize());
        webcamPanel.setMirrored(true);
        jPanel.add(webcamPanel);
        jFrame.add(jPanel);
        jFrame.setVisible(true);

    }
}
