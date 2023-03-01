package org.example.robot;

import lejos.hardware.Audio;
import lejos.remote.ev3.RemoteEV3;

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

}
