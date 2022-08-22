package com.example.gioia.rnds1;

public class UserSettings {

String StateID;
Integer isWindowsOpen;
Integer isDoorOpen;
Integer isLightsOn;
Integer noiseLevel;
String customisedDescription;

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public String getStateID() {
        return StateID;
    }

    public Integer getWindowsOpen() {
        return isWindowsOpen;
    }

    public void setWindowsOpen(Integer windowsOpen) {
        isWindowsOpen = windowsOpen;
    }

    public Integer getDoorOpen() {
        return isDoorOpen;
    }

    public void setDoorOpen(Integer doorOpen) {
        isDoorOpen = doorOpen;
    }

    public Integer getLightsOn() {
        return isLightsOn;
    }

    public void setLightsOn(Integer lightsOn) {
        isLightsOn = lightsOn;
    }

    public Integer getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(Integer noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public String getCustomisedDescription() {
        return customisedDescription;
    }

    public void setCustomisedDescription(String customisedDescription) {
        this.customisedDescription = customisedDescription;
    }
}
