package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecoveryPoint {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("waypoint")
    @Expose
    private WaypointConfig waypoint;

    public RecoveryPoint(int id, int waypoint_id){
        this.id = id;
        this.waypoint = new WaypointConfig(waypoint_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WaypointConfig getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(WaypointConfig waypoint) {
        this.waypoint = waypoint;
    }
}
