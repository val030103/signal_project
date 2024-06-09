package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;

/**
 * Strategy for checking heart rate alerts based on patient records.
 */
public class HeartRateStrategy implements AlertStrategy {

    /**
     * Checks if any heart rate alert conditions are met based on the provided patient records.
     *
     * @param records the list of patient records to evaluate
     * @return true if an alert condition is met, false otherwise
     */
    @Override
    public boolean checkAlert(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("HeartRate")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue > 100 || currentValue < 60) { // Example threshold values
                    return true;
                }
            }
        }
        return false;
    }
}