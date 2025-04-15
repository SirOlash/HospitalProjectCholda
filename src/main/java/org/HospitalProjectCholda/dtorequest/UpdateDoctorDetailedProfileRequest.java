package org.HospitalProjectCholda.dtorequest;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateDoctorDetailedProfileRequest {
    private String firstName;
    private String lastName;

    @Pattern(regexp = "^(\\+234|0)[789][01]\\d{8}$", message = "Invalid phone number format")
    private String phoneNumber;
    private String address;

}
