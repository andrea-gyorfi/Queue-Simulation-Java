package com.example.assignment2.model;

public class Task {
    private int arrivalTime;
    private int serviceTime;
    private int taskId;
    private int startProcessTime;

    public Task(int arrivalTime, int serviceTime, int taskId) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.taskId = taskId;
        this.startProcessTime = 0;
    }


    public int getArrivalTime() {
        return arrivalTime;
    }


    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getStartProcessTime() {
        return startProcessTime;
    }

    public void setStartProcessTime(int startProcessTime) {
        this.startProcessTime = startProcessTime;
    }
}
