package com.example.moveui.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskCtrl {
    @SerializedName("task_definition_id")
    @Expose
    private int task_definition;
    @SerializedName("goals")
    @Expose
    private List<GoalCtrl> goals;
    @SerializedName("priority")
    @Expose
    private int priority;

    public TaskCtrl(int task_definition, List<GoalCtrl> goals, int priority){
        this.task_definition = task_definition;
        this.goals = goals;
        this.priority = priority;
    }

    public int getTask_definition() {
        return task_definition;
    }

    public void setTask_definition(int task_definition) {
        this.task_definition = task_definition;
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
