package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmrStatus {
    @SerializedName("amr_id")
    @Expose
    private int amr_id;
    @SerializedName("navigation_state")
    @Expose
    private String navigation_state;

    public AmrStatus(int amr_id, String navigation_state){
        this.amr_id = amr_id;
        this.navigation_state = navigation_state;
    }

    public String getNavigation_state() {
        return navigation_state;
    }

    public void setNavigation_state(String navigation_state) {
        this.navigation_state = navigation_state;
    }

    public int getAmr_id() {
        return amr_id;
    }

    public void setAmr_id(int amr_id) {
        this.amr_id = amr_id;
    }
}
