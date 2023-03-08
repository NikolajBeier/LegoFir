import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ColorTest {
    @Test
    public void colorSpectrumTest() {
        String ip = "172.20.10.9";
        RemoteEV3 ev3;
        try {
            ev3 = new RemoteEV3(ip);
            RMISampleProvider colorSensor = ev3.createSampleProvider("S1", "lejos.hardware.sensor.EV3ColorSensor", "RGB");

            int testIntervalMs = 1000;
            int testDurationMs = 10000;
            while(testDurationMs > 0) {
                float[] sample = colorSensor.fetchSample();
                System.out.println("R: " + sample[0] + " G: " + sample[1] + " B: " + sample[2]);
                testDurationMs -= testIntervalMs;
                Thread.sleep(testIntervalMs);
            }

            float[] sample = colorSensor.fetchSample();
            System.out.println("R: " + sample[0] + " G: " + sample[1] + " B: " + sample[2]);
            colorSensor.close();
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
