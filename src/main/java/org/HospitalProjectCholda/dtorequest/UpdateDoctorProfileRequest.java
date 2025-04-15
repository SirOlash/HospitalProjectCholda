package org.HospitalProjectCholda.dtorequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateDoctorProfileRequest {
    private String userName;

    @Email(message = "Email must be valid!")
    private String email;
    private String specialty;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be at least 8 characters with at least one letter and one number")
    private String password;
}
