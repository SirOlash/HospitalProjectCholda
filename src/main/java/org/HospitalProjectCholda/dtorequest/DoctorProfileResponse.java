package org.HospitalProjectCholda.dtorequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class DoctorProfileResponse {
        private String userName;
        private String email;
        private String specialty;
        private String password = "*************";
    }

