package com.example.assignment2.businesslogic;

import com.example.assignment2.model.Server;
import com.example.assignment2.model.Task;

import java.util.List;

public interface Strategy {

    public void addTask(List<Server> servers, Task task);

}
