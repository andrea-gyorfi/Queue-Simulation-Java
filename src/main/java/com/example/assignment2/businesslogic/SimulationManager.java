package com.example.assignment2.businesslogic;

import com.example.assignment2.gui.HelloController;
import com.example.assignment2.model.Server;
import com.example.assignment2.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
    private int simulationTimeLimit;
    private int maxServiceTime;
    private int minServiceTime;
    private int maxArrivalTime;
    private int minArrivalTime;
    private int numberOfServers;
    private int numberOfClients;
    private int maxTasksPerServer;

    private int currentTime = 0;
    private Scheduler scheduler;
    private Singleton logEvents;
    private HelloController controller;
    private List<Task> generatedTasks;
    private List<Task> waitingTasks;

    private double avgWaitingTime=0;
    private double avgServiceTime=0;
    private int peakHour=0;
    private int peakHourQs=0;

    public SimulationManager(int simulationTimeLimit, int maxServiceTime, int minServiceTime, int maxArrivalTime, int minArrivalTime, int numberOfServers, int numberOfClients, Scheduler.SelectionPolicy selectionPolicy, HelloController controller) {
        try {
            new java.io.PrintWriter("output.txt").close(); // clear the output file
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.controller = controller;
        this.logEvents = Singleton.getInstance("output.txt");  // thread-safe instance


        this.simulationTimeLimit = simulationTimeLimit;
        this.maxServiceTime = maxServiceTime;
        this.minServiceTime = minServiceTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.maxTasksPerServer = numberOfClients;

        this.scheduler = new Scheduler(numberOfServers, numberOfClients);  // create servers and their threads
        this.scheduler.changeStrategy(selectionPolicy);   // set the strategy

        generateNRandomTasks();  // generate the random tasks
    }


    private synchronized void generateNRandomTasks() {
        this.generatedTasks = new ArrayList<>();
        this.waitingTasks = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= numberOfClients; i++) {
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int processingTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            Task task = new Task(arrivalTime, processingTime, i);  // create the task
            generatedTasks.add(task);
        }

        // sort tasks by arrival time
        Collections.sort(generatedTasks, (task1, task2) -> Integer.compare(task1.getArrivalTime(), task2.getArrivalTime()));
        waitingTasks.addAll(generatedTasks);
    }

    @Override
    public synchronized void run() {
        logState(); // initial state => Time 0

        // calculate the average service time
        for(Task task : generatedTasks) {
            avgServiceTime += task.getServiceTime();
        }
        avgServiceTime /= numberOfClients;

        // until the simulation doesn't get to the set time limit
        while (currentTime < simulationTimeLimit) {
            currentTime++;

            try {
                Thread.sleep(1000); // wait 1 second => give time for servers to process/remove tasks
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // tasks to be removed from waiting list
            List<Task> tasksToDispatch = new ArrayList<>();

            for (Task task : waitingTasks) {
                if (task.getArrivalTime() == currentTime) {
                    // scheduler adds task to server based on strategy
                    scheduler.dispatchTask(task);

                    // make the sum of the waiting times of the tasks (waiting time => time task waits until it gets first in queue)
                    avgWaitingTime += task.getStartProcessTime();

                    // go through all queues and get the sum of their sizes (at the current time)
                    int peakHourQueues=0;
                    for(Server server : scheduler.getServers()){
                        peakHourQueues+=server.getQueueSize();
                    }

                    // find the biggest sum from all the simulation times
                    if(peakHourQueues>peakHourQs){
                        peakHourQs=peakHourQueues;
                        peakHour=currentTime;  // keep the time/hour with most of the tasks in queues
                    }

                    tasksToDispatch.add(task);  // this task was dispatched => can be removed from waiting list
                }
            }

            waitingTasks.removeAll(tasksToDispatch);  // remove dispatched tasks

            logState();   // display the status at time = currentTime
        }

        stopServersThreads();  // stop the servers' threads at the end
    }

    private void logState() {
        StringBuilder logMessage = new StringBuilder();

        logMessage.append("Time ").append(currentTime).append("\n");
        logMessage.append("Waiting clients: ");
        for (Task task : waitingTasks) {
                logMessage.append("(").append(task.getTaskId()).append(",").append(task.getArrivalTime()).append(",").append(task.getServiceTime()).append(") ");
        }
        logMessage.append("\n");

        for (int i = 0; i < numberOfServers; i++) {
            Server server = scheduler.getServers().get(i);
            logMessage.append("Queue ").append(i + 1).append(": ");
            List<Task> tasks = server.getTasks();
            if (tasks.isEmpty()) {
                logMessage.append("closed\n");
            } else {
                for (Task task : tasks) {
                    logMessage.append("(").append(task.getTaskId()).append(",").append(task.getArrivalTime()).append(",").append(task.getServiceTime()).append(") ");
                }
                logMessage.append("\n");
            }
        }

        if(currentTime == simulationTimeLimit) {
            avgWaitingTime = avgWaitingTime/numberOfClients;  // calculate average of waiting time
            logMessage.append("\n");
            logMessage.append("Average waiting time: ").append(avgWaitingTime).append("\n");
            logMessage.append("Average service time: ").append(avgServiceTime).append("\n");
            logMessage.append("Peak hour: ").append(peakHour).append("\n");
        }

        String message = logMessage.toString();
        logEvents.log(message);  // display log of events to output file

        if (controller != null) {
            controller.addSimulationLog(message);  // also display to GUI
        }
    }


    private void stopServersThreads() {
        // stop each server's thread
        for (Server server : scheduler.getServers()) {
            server.stopServerThread();
        }
    }
}
