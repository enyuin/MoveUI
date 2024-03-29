package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTaskCtrl {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("amr_id")
    @Expose
    private int amr_id;
    @SerializedName("state")
    @Expose
    private String state;

    public GetTaskCtrl(int id, int amr_id, String state){
        this.id = id;
        this.amr_id = amr_id;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmr_id() {
        return amr_id;
    }

    public void setAmr_id(int amr_id) {
        this.amr_id = amr_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
