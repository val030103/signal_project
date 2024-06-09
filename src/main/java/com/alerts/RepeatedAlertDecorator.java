package com.alerts;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Decorator class for repeating alerts at a specified interval.
 */
public class RepeatedAlertDecorator extends AlertDecorator {
    private int interval; // Interval in seconds

    /**
     * Constructs a RepeatedAlertDecorator.
     *
     * @param decoratedAlert the alert to be decorated
     * @param interval the interval in seconds at which the alert should repeat
     */
    public RepeatedAlertDecorator(Alert decoratedAlert, int interval) {
        super(decoratedAlert);
        this.interval = interval;
        startRepeating();
    }

    /**
     * Starts the timer to repeat the alert at the specified interval.
     */
    private void startRepeating() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                notifyAlert();
            }
        }, 0, interval * 1000);
    }

    /**
     * Notifies about the decorated alert and includes the repetition information.
     */
    @Override
    public void notifyAlert() {
        super.notifyAlert();
        System.out.println("This alert will repeat every " + interval + " seconds.");
    }
}