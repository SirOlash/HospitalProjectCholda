package org.HospitalProjectCholda.dtorequest;


import lombok.Data;
import org.HospitalProjectCholda.data.models.Gender;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class PatientProfileDetailRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private Gender gender;

}
