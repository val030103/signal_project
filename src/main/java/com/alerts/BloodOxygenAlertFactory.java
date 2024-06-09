package com.alerts;

/**
 * Factory class for creating BloodOxygenAlert instances.
 */
public class BloodOxygenAlertFactory extends AlertFactory {
    /**
     * Creates a new BloodOxygenAlert.
     *
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition triggering the alert
     * @param timestamp the time the alert was triggered, in milliseconds since epoch
     * @return the created BloodOxygenAlert
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}