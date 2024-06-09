package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {

    @Override
    public boolean checkAlert(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodSaturation")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue < 92) {
                    return true;
                }
                double previousValue = 0;
                long previousTimestamp = 0;
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