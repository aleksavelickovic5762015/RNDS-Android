package com.example.gioia.rnds1;

import java.util.List;

public class UserStatus {
    private String userID;
    private String userName;
    private String currentStatusID;
    private String currentStatus;
    private Integer disturbing;
   // private List<String> availableStates;
    private Integer noiseLevel;


    public String getCurrentStatusID() {
        return currentStatusID;
    }

    public void setCurrentStatusID(String currentStatusID) {
        this.currentStatusID = currentStatusID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(Integer noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public Integer isDisturbing() {
        return disturbing;
    }

    public void setDisturbing(Integer disturbing) {
        disturbing = disturbing;
    }
}
