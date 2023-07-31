package com.example.moveui.data.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObstacleStatus {
    @SerializedName("environment")
    @Expose
    private List<Integer> environment;
    @SerializedName("amr")
    @Expose
    private List<Integer> amr;

    public ObstacleStatus(List<Integer> environment, List<Integer> amr){
        this.environment = environment;
        this.amr = amr;
    }

    public List<Integer> getEnvironment() {
        return environment;
    }

    public void setEnvironment(List<Integer> environment) {
        this.environment = environment;
    }

    public List<Integer> getAmr() {
        return amr;
    }

    public void setAmr(List<Integer> amr) {
        this.amr = amr;
    }
}
