package com.alerts;

public class ECGAlert extends Alert {
    public ECGAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    // Additional methods specific to ECG Alerts can be added here
}