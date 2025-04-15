package org.HospitalProjectCholda.dtorequest;

import lombok.*;

import java.util.HashMap;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientUpdateResponse {

    private Map<String, String> updatedFields = new HashMap<>();
    private Map<String, String> failedFields = new HashMap<>();
    private Map<String, String> unchangedFields = new HashMap<>();


}







