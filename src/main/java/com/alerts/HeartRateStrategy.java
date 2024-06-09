package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy {

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