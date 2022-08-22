package com.example.gioia.rnds1;

public class NoiseLevel {
    private String userID;
    private String noiseLevel;
    private Integer isDisturbing;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(String noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public Integer getIsDisturbing() {
        return isDisturbing;
    }

    public void setIsDisturbing(Integer isDisturbing) {
        this.isDisturbing = isDisturbing;
    }
}
