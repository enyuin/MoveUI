package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoalCtrl {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("goto")
    @Expose
    private GoToCtrl goto_tree;

    public GoalCtrl(String id, GoToCtrl goto_tree){
        this.id = id;
        this.goto_tree = goto_tree;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GoToCtrl getGoto_tree() {
        return goto_tree;
    }

    public void setGoto_tree(GoToCtrl goto_tree) {
        this.goto_tree = goto_tree;
    }
}
