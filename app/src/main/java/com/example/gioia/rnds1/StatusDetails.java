package com.example.gioia.rnds1;

public class StatusDetails {
    private String statusdesctiption;
    private boolean isWindowsOpen;
    private boolean isDoorOpen;
    private boolean isLightsOn;
    private int noiseLevel;

    public String getStatusdesctiption() {
        return statusdesctiption;
    }

    public void setStatusdesctiption(String statusdesctiption) {
        this.statusdesctiption = statusdesctiption;
    }

    public boolean isWindowsOpen() {
        return isWindowsOpen;
    }

    public void setWindowsOpen(boolean windowsOpen) {
        isWindowsOpen = windowsOpen;
    }

    public boolean isDoorOpen() {
        return isDoorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        isDoorOpen = doorOpen;
    }

    public boolean isLightsOn() {
        return isLightsOn;
    }

    public void setLightsOn(boolean lightsOn) {
        isLightsOn = lightsOn;
    }

    public int getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(int noiseLevel) {
        this.noiseLevel = noiseLevel;
    }
}
