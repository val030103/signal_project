package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {

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
