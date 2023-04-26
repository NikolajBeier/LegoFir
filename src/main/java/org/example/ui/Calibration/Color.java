package org.example.ui.Calibration;

public class Color {
    private int hueMin, hueMax, valMin, valMax, satMin, satMax;

    public Color(){

    }

    public Color(int hueMin, int hueMax, int valMin, int valMax, int satMin, int satMax) {
        this.hueMin = hueMin;
        this.hueMax = hueMax;
        this.valMin = valMin;
        this.valMax = valMax;
        this.satMin = satMin;
        this.satMax = satMax;
    }

    public int getHueMin() {
        return hueMin;
    }

    public void setHueMin(int hueMin) {
        this.hueMin = hueMin;
    }

    public int getHueMax() {
        return hueMax;
    }

    public void setHueMax(int hueMax) {
        this.hueMax = hueMax;
    }

    public int getValMin() {
        return valMin;
    }

    public void setValMin(int valMin) {
        this.valMin = valMin;
    }

    public int getValMax() {
        return valMax;
    }

    public void setValMax(int valMax) {
        this.valMax = valMax;
    }

    public int getSatMin() {
        return satMin;
    }

    public void setSatMin(int satMin) {
        this.satMin = satMin;
    }

    public int getSatMax() {
        return satMax;
    }

    public void setSatMax(int satMax) {
        this.satMax = satMax;
    }
}
