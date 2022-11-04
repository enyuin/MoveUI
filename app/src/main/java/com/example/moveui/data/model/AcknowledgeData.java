package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcknowledgeData {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("data")
    @Expose
    private String data;

    public AcknowledgeData(int id, String data){
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
