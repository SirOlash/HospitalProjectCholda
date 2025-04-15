package org.HospitalProjectCholda.dtorequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import lombok.Data;

@Data
public class DoctorRegistrationRequest {

    @NotBlank(message = "username cannot be blank!")
    private String userName;

    @Email(message = "Email must be valid!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be at least 8 characters with at least one letter and one number")
    private String password;


    @NotBlank(message = "Specialisation cannot be empty!")
    private String specialty;

    public void setAvailable(boolean b) {
        b = true;
    }



//    private boolean available = true;


}
