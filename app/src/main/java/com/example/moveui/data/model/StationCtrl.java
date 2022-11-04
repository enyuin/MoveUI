package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StationCtrl {
    @SerializedName("ids")
    @Expose
    private int[] ids;

    public StationCtrl(int id){
        this.ids = new int[id];
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }
}
