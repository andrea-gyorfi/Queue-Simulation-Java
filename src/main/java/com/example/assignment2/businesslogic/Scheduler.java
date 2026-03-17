package com.example.assignment2.businesslogic;

import com.example.assignment2.model.Server;
import com.example.assignment2.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    // strategy types
    public enum SelectionPolicy {
        SHORTEST_QUEUE, SHORTEST_TIME;
    }

    public Scheduler( int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        servers = new ArrayList<Server>();

        // create the servers/queues and start thread for each
        for(int i = 0; i < maxNoServers; i++){
            Server server = new Server(maxTasksPerServer);
            Thread thread = new Thread(server);
            thread.start();
            servers.add(server);
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        // select the strategy
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ShortestQueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task task) {
        // if a strategy was chosen => add task to servers based on that strategy
        if(strategy != null){
            strategy.addTask(servers, task);
        }
        else{
            System.out.println("No strategy selected");
        }
    }

    public List<Server> getServers() {
        return servers;
    }
}
