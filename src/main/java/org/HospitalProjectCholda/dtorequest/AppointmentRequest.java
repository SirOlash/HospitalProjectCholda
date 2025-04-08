package org.HospitalProjectCholda.dtorequest;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.HospitalProjectCholda.data.models.Patient;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest{
    private Patient patient;

    @NotEmpty(message = "email cannot be empty!")
    @Email(message = "email must be valid!")
    private String doctorEmail;

    @NotNull(message = "Description cannot be empty!")
    private String description;


//    @JsonFormat(pattern = "yyyy, MM, dd, HH, mm")
//    private LocalDateTime appointmentTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentTime;

}
