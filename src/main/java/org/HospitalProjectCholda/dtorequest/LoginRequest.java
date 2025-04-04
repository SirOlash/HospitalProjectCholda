package org.HospitalProjectCholda.dtorequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @NotEmpty(message = "email cannot be empty!")
    @Email(message = "email must be valid!")
    private String email;

    @NotEmpty(message = "password cannot be empty!")
    private String password;

}
