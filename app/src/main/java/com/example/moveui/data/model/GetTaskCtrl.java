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

    public GetTaskCtrl(int id, int amr_id){
        this.id = id;
        this.amr_id = amr_id;
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
}
