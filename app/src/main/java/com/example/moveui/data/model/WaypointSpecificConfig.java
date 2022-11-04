package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaypointSpecificConfig {
    @SerializedName("theta")
    @Expose
    private double theta;

    public WaypointSpecificConfig(double theta){
        this.theta = theta;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
