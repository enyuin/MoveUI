package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoalCtrl {
    @SerializedName("index")
    @Expose
    private int index;
    @SerializedName("waypoint_id")
    @Expose
    private int waypoint_id;
    @SerializedName("action_tree")
    @Expose
    private int action_tree;
    @SerializedName("pickup")
    @Expose
    private int[] pickup;
    @SerializedName("delivery")
    @Expose
    private int[] delivery;

    public GoalCtrl(int index, int waypoint_id, int action_tree){
        this.index = index;
        this.waypoint_id = waypoint_id;
        this.action_tree = action_tree;
        this.pickup = new int[]{};
        this.delivery = new int[]{};
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getWaypoint_id() {
        return waypoint_id;
    }

    public void setWaypoint_id(int waypoint_id) {
        this.waypoint_id = waypoint_id;
    }

    public int getAction_tree() {
        return action_tree;
    }

    public void setAction_tree(int action_tree) {
        this.action_tree = action_tree;
    }

    public int[] getPickup() {
        return pickup;
    }

    public void setPickup(int[] pickup) {
        this.pickup = pickup;
    }
}
