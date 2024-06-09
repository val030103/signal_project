package com.alerts;

/**
 * Represents an alert specifically for blood pressure conditions.
 */
public class BloodPressureAlert extends Alert {
    /**
     * Constructs a new BloodPressureAlert.
     *
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition triggering the alert
     * @param timestamp the time the alert was triggered, in milliseconds since epoch
     */
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    // Additional methods specific to Blood Pressure Alerts can be added here
}