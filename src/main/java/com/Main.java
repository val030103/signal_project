package com;

import com.cardio_generator.HealthDataSimulator;

import java.io.IOException;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            switch (args[0]) {
                case "DataStorage":
                    DataStorage.main(new String[]{});
                    break;
                case "HealthDataSimulator":
                    HealthDataSimulator.main(new String[]{});
                    break;
                case "AlertGenerator":
                    AlertGenerator.main(new String[]{});
                    break;
                default:
                    System.out.println("Invalid argument. Please use 'DataStorage', 'HealthDataSimulator', or 'AlertGenerator'.");
            }
        } else {
            System.out.println("Please provide a class name to run: 'DataStorage', 'HealthDataSimulator', or 'AlertGenerator'.");
        }
    }
}