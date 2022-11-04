package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModeCtrl {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("mode")
    @Expose
    private String mode;

    public ModeCtrl(int id, String mode){
        this.id = id;
        this.mode = mode;
    }

    public int getId() {
        return id;
    }

    public void setId(String user_name) {
        this.id = id;
    }

    public void setMode(String password) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

}
