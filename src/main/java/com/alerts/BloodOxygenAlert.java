package com.alerts;

/**
 * Represents an alert specifically for blood oxygen conditions.
 */
public class BloodOxygenAlert extends Alert {
    /**
     * Constructs a new BloodOxygenAlert.
     *
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition triggering the alert
     * @param timestamp the time the alert was triggered, in milliseconds since epoch
     */
    public BloodOxygenAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    // Additional methods specific to Blood Oxygen Alerts can be added here
}