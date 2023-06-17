package org.example.robot.music;

import lejos.hardware.Audio;
import lejos.remote.ev3.RemoteEV3;

import java.io.File;

/**
 * This class is used to define different themes for the robot at startup. They can then be called in the launcher as
 * such: Themes.themeName(RemoteEV3 object)
 */

public class Themes {

    public void OriginalTheme(RemoteEV3 ev3){

        Audio sound = ev3.getAudio();
        sound.setVolume(15);

        int i;
        for (i = 0; i < 3; ++i) {
            sound.playTone(2500, 100);
            sound.playTone(500, 100);
        }

        for (i = 0; i < 4; ++i) {
            sound.playTone(300, 200);
        }

        for (i = 0; i < 3; ++i) {
            sound.playTone(2500, 100);
            sound.playTone(100, 100);
        }

        for (i = 0; i < 4; ++i) {
            sound.playTone(300, 200);
        }

        for (i = 0; i < 3; ++i) {
            sound.playTone(2500, 100);
            sound.playTone(400, 100);
        }
    }

    // work in progress

    public void TetrisTheme(RemoteEV3 ev3) {

        Audio sound = ev3.getAudio();
        sound.setVolume(15);

        sound.playTone(329, 500);  // e
        sound.playTone(466, 250); // b
        sound.playTone(261, 250); //c
        sound.playTone(294, 500); //d
        sound.playTone(261, 250); //c
        sound.playTone(466, 250); // b
        sound.playTone(440, 500); //a

    }

    public void ImperialTheme(RemoteEV3 ev3) {

        Audio sound = ev3.getAudio();
        sound.setVolume(15);

        for (int i = 0; i < 3; i++) {
            sound.playTone(440,500);
        }
        sound.playTone(349,350);
        sound.playTone(523,150);

        sound.playTone(440,500);
        sound.playTone(349,350);
        sound.playTone(523,150);

        sound.playTone(440,1000);
    }

    public void ohmygod(RemoteEV3 ev3) {
        try {
            Audio sound = ev3.getAudio();
            sound.setVolume(100);
            System.out.println(sound.playSample(new File("oh-my-godv4.wav"), 100));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
