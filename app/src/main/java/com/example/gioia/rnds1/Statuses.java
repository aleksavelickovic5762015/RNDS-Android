package com.example.gioia.rnds1;

public class Statuses {
    String statusID;
    String statusDescription;

    public String getStatusID() {
        return statusID;
    }


    public String getStatusDescription() {
        return statusDescription;
    }

    @Override
    public String toString() {
        return  statusDescription;
    }
}
