package com.alerts;

/**
 * Represents an alert specifically for ECG conditions.
 */
public class ECGAlert extends Alert {
    /**
     * Constructs a new ECGAlert.
     *
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition triggering the alert
     * @param timestamp the time the alert was triggered, in milliseconds since epoch
     */
    public ECGAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    // Additional methods specific to ECG Alerts can be added here
}