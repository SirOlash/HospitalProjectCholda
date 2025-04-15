package org.HospitalProjectCholda.dtorequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class PatientDetailedUpdatedProfileResponse  {

        private Map<String, String> updatedFields = new HashMap<>();
        private Map<String, String> failedFields = new HashMap<>();
        private Map<String, String> unchangedFields = new HashMap<>();
}
