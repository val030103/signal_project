package com.alerts;

/**
 * Represents an alert with details about the patient, condition, and timestamp.
 */
public class Alert {
    private String patientId;
    private String condition;
    private long timestamp;

    /**
     * Constructs a new Alert.
     *
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition triggering the alert
     * @param timestamp the time the alert was triggered, in milliseconds since epoch
     */
    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Gets the patient ID associated with this alert.
     *
     * @return the patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the condition triggering this alert.
     *
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gets the timestamp of when this alert was triggered.
     *
     * @return the timestamp in milliseconds since epoch
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Notifies about this alert, typically printing the alert details.
     */
    public void notifyAlert() {
        System.out.println("Alert: " + condition + " for patient " + patientId + " at " + timestamp);
    }
}