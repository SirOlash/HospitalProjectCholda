package org.HospitalProjectCholda.dtorequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientRegistrationRequest {

    @NotBlank(message = "username cannot be blank!")
    private String userName;

    @Email(message = "Email must be valid!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters!")
    @NotBlank(message = "Password cannot be empty!")
    private String password;


}
