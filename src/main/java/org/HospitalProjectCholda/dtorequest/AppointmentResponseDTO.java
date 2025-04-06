package org.HospitalProjectCholda.dtorequest;

import lombok.Data;
import org.HospitalProjectCholda.data.models.Patient;

import java.time.LocalDate;

@Data
public class AppointmentResponseDTO {

    private String appointmentId;
    private String doctorEmail;
    private String description;
    private LocalDate appointmentTime;


}
