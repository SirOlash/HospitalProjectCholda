package org.HospitalProjectCholda.dtorequest;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String doctorEmail;
    private String description;


    @JsonFormat(pattern = "yyyy, MM, dd, HH, mm")
    private LocalDateTime appointmentTime;

}
