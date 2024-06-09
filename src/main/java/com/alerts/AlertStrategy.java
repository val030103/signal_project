package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;

/**
 * Strategy interface for checking alerts based on patient records.
 */
public interface AlertStrategy {
    /**
     * Checks if any alert conditions are met based on the provided patient records.
     *
     * @param records the list of patient records to evaluate
     * @return true if an alert condition is met, false otherwise
     */
    boolean checkAlert(List<PatientRecord> records);
}