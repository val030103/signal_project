package com.alerts;

/**
 * Factory class for creating ECGAlert instances.
 */
public class ECGAlertFactory extends AlertFactory {
    /**
     * Creates a new ECGAlert.
     *
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition triggering the alert
     * @param timestamp the time the alert was triggered, in milliseconds since epoch
     * @return the created ECGAlert
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}