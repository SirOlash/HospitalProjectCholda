package org.HospitalProjectCholda.dtorequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.HospitalProjectCholda.data.models.Gender;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PatientDetailedProfileResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private Gender gender;
    private LocalDate dateOfBirth;

    public PatientDetailedProfileResponse(String firstName, String lastName, String phoneNumber, String address, LocalDate dateOfBirth, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
}
