package com.alerts;

/**
 * Factory class for creating BloodPressureAlert instances.
 */
public class BloodPressureAlertFactory extends AlertFactory {
    /**
     * Creates a new BloodPressureAlert.
     *
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition triggering the alert
     * @param timestamp the time the alert was triggered, in milliseconds since epoch
     * @return the created BloodPressureAlert
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}