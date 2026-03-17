# Queue Simulation (Java)

A Java application that simulates a queue management system using multithreading and synchronization. Clients are dynamically assigned to multiple servers based on different scheduling strategies to minimize waiting time and improve efficiency.

## Features
- Multiple queues (servers running on separate threads)
- Task scheduling strategies:
  - Shortest Queue
  - Shortest Time
- Real-time simulation
- Performance metrics:
  - Average waiting time
  - Average service time
  - Peak hour
- JavaFX graphical interface

## Technologies
- Java 17
- JavaFX
- Maven
- Multithreading & synchronization (BlockingQueue, AtomicInteger)

## Description

The application simulates how clients are distributed across multiple queues over time. Each queue is processed by a separate thread, while a central scheduler assigns tasks based on selected strategies.

The goal is to minimize waiting time and analyze system performance under different workloads.

## Notes
- Tasks are generated randomly within given time intervals
- Thread-safe data structures are used to ensure correct behavior
- Simulation results are logged and displayed in real time

---

This project was built to practice concurrent programming, thread synchronization, and basic GUI development in Java.
