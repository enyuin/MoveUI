package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskCtrl {
    @SerializedName("workflow_id")
    @Expose
    private int workflow_id;
    @SerializedName("amr_id")
    @Expose
    private int amr_id;
    @SerializedName("goals")
    @Expose
    private List<GoalCtrl> goals;
    @SerializedName("priority")
    @Expose
    private int priority;

    public TaskCtrl(int workflow_id, List<GoalCtrl> goals, int priority){
        this.workflow_id = workflow_id;
        this.goals = goals;
        this.priority = priority;
        this.amr_id = 1;
    }

    public int getWorkflow_id() {
        return workflow_id;
    }

    public void setWorkflow_id(int workflow_id) {
        this.workflow_id = workflow_id;
    }

    public List<GoalCtrl> getGoals() {
        return goals;
    }

    public void setGoals(List<GoalCtrl> goals) {
        this.goals = goals;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
