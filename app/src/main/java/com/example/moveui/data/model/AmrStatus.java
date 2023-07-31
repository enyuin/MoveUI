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
    @SerializedName("battery_level")
    @Expose
    private int battery_level;
    @SerializedName("system_status")
    @Expose
    private SystemStatus system_status;
    @SerializedName("curr_waypoint_id")
    @Expose
    private int curr_waypoint_id;

    public AmrStatus(int amr_id, String navigation_state, int battery_level, SystemStatus system, int curr_waypoint_id){
        this.amr_id = amr_id;
        this.navigation_state = navigation_state;
        this.battery_level = battery_level;
        this.system_status = system;
        this.curr_waypoint_id = curr_waypoint_id;
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

    public int getBattery_level() {
        return battery_level;
    }

    public void setBattery_level(int battery_level) {
        this.battery_level = battery_level;
    }

    public SystemStatus getSystem_status() {
        return system_status;
    }

    public void setSystem_status(SystemStatus system_status) {
        this.system_status = system_status;
    }

    public int getCurr_waypoint_id() {
        return curr_waypoint_id;
    }

    public void setCurr_waypoint_id(int curr_waypoint_id) {
        this.curr_waypoint_id = curr_waypoint_id;
    }
}
