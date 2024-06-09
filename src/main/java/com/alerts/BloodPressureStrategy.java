package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;

/**
 * Strategy for checking blood pressure alerts based on patient records.
 */
public class BloodPressureStrategy implements AlertStrategy {

    /**
     * Checks if any blood pressure alert conditions are met based on the provided patient records.
     *
     * @param records the list of patient records to evaluate
     * @return true if an alert condition is met, false otherwise
     */
    @Override
    public boolean checkAlert(List<PatientRecord> records) {
        int consecutiveCount = 0;
        double previousValue = 0;
        boolean increasingTrend = false;
        boolean decreasingTrend = false;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodPressure")) {
                double currentValue = record.getMeasurementValue();
                if (consecutiveCount == 0) {
                    previousValue = currentValue;
                    consecutiveCount++;
                } else {
                    double difference = currentValue - previousValue;
                    if (difference > 10) {
                        if (increasingTrend || consecutiveCount == 1) {
                            increasingTrend = true;
                            consecutiveCount++;
                        } else {
                            increasingTrend = false;
                            consecutiveCount = 1;
                        }
                    } else if (difference < -10) {
                        if (decreasingTrend || consecutiveCount == 1) {
                            decreasingTrend = true;
                            consecutiveCount++;
                        } else {
                            decreasingTrend = false;
                            consecutiveCount = 1;
                        }
                    } else {
                        increasingTrend = false;
                        decreasingTrend = false;
                        consecutiveCount = 1;
                    }
                    previousValue = currentValue;
                }

                if (consecutiveCount >= 3) {
                    return true;
                }
            }
        }

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodPressure")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue > 180 || currentValue < 90) {
                    return true;
                }
            }
        }
        return false;
    }
}