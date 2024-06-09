package com.alerts;

import java.util.Timer;
import java.util.TimerTask;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int interval; // Interval in seconds

    public RepeatedAlertDecorator(Alert decoratedAlert, int interval) {
        super(decoratedAlert);
        this.interval = interval;
        startRepeating();
    }

    private void startRepeating() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                notifyAlert();
            }
        }, 0, interval * 1000);
    }

    @Override
    public void notifyAlert() {
        super.notifyAlert();
        System.out.println("This alert will repeat every " + interval + " seconds.");
    }
}