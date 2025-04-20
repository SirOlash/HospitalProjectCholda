package org.FirstAuctionSystem.data.dtorequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class BuyerRegistrationRequest {

    @NotBlank(message = "UserName cannot be blank!")
    private String userName;

    @Email(message = "Email must be valid!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be at least 8 characters with at least one letter and one number")
    private String password;
}
