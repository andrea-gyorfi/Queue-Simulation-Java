package com.example.assignment2.businesslogic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class Singleton {

    private static volatile Singleton instance;
    public String value;

    private Singleton(String value) {
        this.value = value;
    }

    public static Singleton getInstance(String value) {
        Singleton result = instance;
        if (result != null) {
            return result;
        }
        synchronized(Singleton.class) {
            if (instance == null) {
                instance = new Singleton(value);
            }
            return instance;
        }
    }


    // helps display log of events => value represents the file
    public void log(String text) {
        try (BufferedWriter writeLog = new BufferedWriter(new FileWriter(value, true))) {
            writeLog.write(text);
            writeLog.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
