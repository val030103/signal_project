package com.alerts;

/**
 * Abstract class for decorating an alert with additional behavior.
 */
public abstract class AlertDecorator extends Alert {
    protected Alert decoratedAlert;

    /**
     * Constructs an AlertDecorator.
     *
     * @param decoratedAlert the alert to be decorated
     */
    public AlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp());
        this.decoratedAlert = decoratedAlert;
    }

    /**
     * Notifies about the decorated alert by delegating to the wrapped alert.
     */
    @Override
    public void notifyAlert() {
        decoratedAlert.notifyAlert();
    }
}