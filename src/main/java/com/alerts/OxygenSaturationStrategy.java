package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;

/**
 * Strategy for checking blood oxygen saturation alerts based on patient records.
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    /**
     * Checks if any blood oxygen saturation alert conditions are met based on the provided patient records.
     *
     * @param records the list of patient records to evaluate
     * @return true if an alert condition is met, false otherwise
     */
    @Override
    public boolean checkAlert(List<PatientRecord> records) {
        double previousValue = 0;
        long previousTimestamp = 0;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodSaturation")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue < 92) {
                    return true;
                }
                if (previousTimestamp != 0 && (record.getTimestamp() - previousTimestamp) <= 600000) {
                    if ((previousValue - currentValue) >= 5) {
                        return true;
                    }
                }
                previousValue = currentValue;
                previousTimestamp = record.getTimestamp();
            }
        }
        return false;
    }
}