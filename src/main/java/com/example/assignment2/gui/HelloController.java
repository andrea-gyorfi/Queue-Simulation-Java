package com.example.assignment2.gui;

import com.example.assignment2.businesslogic.Scheduler;
import com.example.assignment2.businesslogic.SimulationManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField numQueues;
    @FXML
    private TextField numClients;
    @FXML
    private TextField simInterval;
    @FXML
    private TextField minArrivalT;
    @FXML
    private TextField maxArrivalT;
    @FXML
    private TextField minServiceT;
    @FXML
    private TextField maxServiceT;
    @FXML
    private TextField strategyT;
    @FXML
    private ListView<String> simulationListView;

    private SimulationManager simulationManager;

    @FXML
    protected void onClearClick() {
        // clear all the fields
        numQueues.clear();
        numClients.clear();
        simInterval.clear();
        minArrivalT.clear();
        maxArrivalT.clear();
        minServiceT.clear();
        maxServiceT.clear();
        strategyT.clear();

        simulationListView.getItems().clear();
        simulationListView.getItems().add("Fields Cleared!");

    }

    @FXML
    protected void onStartSimulationClick() {
        simulationListView.getItems().clear();

        if (simulationManager == null) {
            simulationListView.getItems().add("Load data before starting the simulation!");
            return;
        }

        // start thread responsible for simulation (start simulation)
        new Thread(simulationManager).start();
    }

    @FXML
    protected void onGetDataClick() {
        try {
            int numberOfQueues = Integer.parseInt(numQueues.getText());
            int numberOfClients = Integer.parseInt(numClients.getText());
            int simulationInterval = Integer.parseInt(simInterval.getText());
            int minArrivalTime = Integer.parseInt(minArrivalT.getText());
            int maxArrivalTime = Integer.parseInt(maxArrivalT.getText());
            int minServiceTime = Integer.parseInt(minServiceT.getText());
            int maxServiceTime = Integer.parseInt(maxServiceT.getText());

            // select policy based on strategy type entered
            String strategyType = strategyT.getText().trim();
            Scheduler.SelectionPolicy selectionPolicy;
            if (strategyType.equalsIgnoreCase("Shortest Queue")) {
                selectionPolicy = Scheduler.SelectionPolicy.SHORTEST_QUEUE;
            }
            else if (strategyType.equalsIgnoreCase("Time")) {
                selectionPolicy = Scheduler.SelectionPolicy.SHORTEST_TIME;
            }
            else{
                simulationListView.getItems().clear();
                simulationListView.getItems().add("Invalid strategy!");
                return;
            }

            // simulation setup
            simulationManager = new SimulationManager(simulationInterval, maxServiceTime, minServiceTime, maxArrivalTime, minArrivalTime, numberOfQueues, numberOfClients, selectionPolicy, this);

            simulationListView.getItems().clear();
            simulationListView.getItems().add("Data Set Successfully!");
        } catch (NumberFormatException e) {
            simulationListView.getItems().clear();
            simulationListView.getItems().add("Please Enter Valid Numbers!");
        }
    }

    public void addSimulationLog(String log) {
        // update log of events on the UI thread (UI has separate thread, need to update it from another thread)
        Platform.runLater(() -> {
            simulationListView.getItems().add(log);
        });
    }
}