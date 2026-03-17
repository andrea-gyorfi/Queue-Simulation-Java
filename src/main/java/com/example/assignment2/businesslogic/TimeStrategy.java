package com.example.assignment2.businesslogic;

import com.example.assignment2.model.Server;
import com.example.assignment2.model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task task){ // find server with the least waiting time
        Server server1 = servers.get(0);

        for (Server server2 : servers) {
            if (server2.getWaitingPeriod() < server1.getWaitingPeriod()) {
                server1 = server2;
            }
        }

        // set waiting time for task then add it to proper server
        task.setStartProcessTime(server1.getWaitingPeriod());
        server1.addTask(task);
    }
}
