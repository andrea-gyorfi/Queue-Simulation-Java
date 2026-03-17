package com.example.assignment2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private BlockingQueue<Task> tasks;   // thread-safe queue
    private AtomicInteger waitingPeriod;   // also ensures thread-safety
    private int maxTasksPerServer;
    private volatile boolean serverThread;

    public Server(int maxTasksPerServer) {
        this.tasks = new LinkedBlockingQueue<Task>();
        this.waitingPeriod = new AtomicInteger(0);
        this.maxTasksPerServer = maxTasksPerServer;
        this.serverThread = true;   // thread has to run when creating the server
    }

    // helps to stop the thread
    public void stopServerThread() {
        serverThread = false;
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public int getQueueSize() {
        return tasks.size();
    }

    public void addTask(Task newTask) {
        // add a task to the server and also increase the waiting period for that server
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());

    }


    @Override
    public void run() {
        // while serverThread is not false
        while (serverThread) {
            try {
                Task task = tasks.peek(); // just peek, don't remove yet
                if (task != null) {
                    synchronized (task) {
                        int currentServiceTime = task.getServiceTime();

                        // if task is still being processed => decrease its service time
                        if (currentServiceTime >= 1) {
                            task.setServiceTime(currentServiceTime - 1);
                            if (task.getServiceTime() == 0 ) {
                                tasks.take(); // done processing that task => remove it
                            }
                        }

                        waitingPeriod.decrementAndGet(); // because task's service time was decreased => server's waiting time decreases
                    }

                    Thread.sleep(1000);  // wait for 1 second (so simulation's thread has time to display)
                }else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    public Task getTask() {
        return tasks.poll();
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public int countQueueSize(){
        int count = 0;
        for (Task task : tasks) {
            if (task.getServiceTime() > 0) {
                count++;
            }
        }
        return count;
    }
}
