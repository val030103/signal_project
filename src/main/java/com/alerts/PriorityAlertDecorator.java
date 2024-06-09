package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {
    private String priority;

    public PriorityAlertDecorator(Alert decoratedAlert, String priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    @Override
    public void notifyAlert() {
        super.notifyAlert();
        System.out.println("Priority: " + priority);
    }
}