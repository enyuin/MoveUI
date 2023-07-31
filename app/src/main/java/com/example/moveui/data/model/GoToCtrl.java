package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoToCtrl {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("waypoint_name")
    @Expose
    private String waypoint_name;

    public GoToCtrl(String id, String waypoint_name){
        this.id = id;
        this.waypoint_name = waypoint_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWaypoint_name() {
        return waypoint_name;
    }

    public void setWaypoint_name(String waypoint_name) {
        this.waypoint_name = waypoint_name;
    }
}
