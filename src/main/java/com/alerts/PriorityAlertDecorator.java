package com.alerts;

/**
 * Decorator class for adding priority to alerts.
 */
public class PriorityAlertDecorator extends AlertDecorator {
    private String priority;

    /**
     * Constructs a PriorityAlertDecorator.
     *
     * @param decoratedAlert the alert to be decorated
     * @param priority the priority level to be added to the alert
     */
    public PriorityAlertDecorator(Alert decoratedAlert, String priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    /**
     * Notifies about the decorated alert and includes the priority information.
     */
    @Override
    public void notifyAlert() {
        super.notifyAlert();
        System.out.println("Priority: " + priority);
    }
}