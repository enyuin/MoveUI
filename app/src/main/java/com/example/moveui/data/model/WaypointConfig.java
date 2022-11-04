package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaypointConfig {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("map_id")
    @Expose
    private String map_id;
    @SerializedName("x")
    @Expose
    private double x;
    @SerializedName("y")
    @Expose
    private double y;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("config")
    @Expose
    private WaypointSpecificConfig config;

    public WaypointConfig(int id){
        this.id = id;
        this.map_id = "a6496fa7-d233-47e0-ba3b-da52c153ea65";
        this.x = 0.08;
        this.y = -0.12;
        this.type = "recovery";
        this.config = new WaypointSpecificConfig(0.0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WaypointSpecificConfig getConfig() {
        return config;
    }

    public void setConfig(WaypointSpecificConfig config) {
        this.config = config;
    }
}
