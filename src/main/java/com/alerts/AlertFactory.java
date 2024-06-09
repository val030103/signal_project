package com.alerts;

/**
 * Abstract factory class for creating alerts.
 */
public abstract class AlertFactory {
    /**
     * Creates an alert with the specified details.
     *
     * @param patientId  the ID of the patient associated with the alert
     * @param condition  the condition triggering the alert
     * @param timestamp  the time the alert was triggered, in milliseconds since epoch
     * @return the created Alert
     */
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}