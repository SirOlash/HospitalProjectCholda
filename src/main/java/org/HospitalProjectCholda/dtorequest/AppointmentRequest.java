package org.HospitalProjectCholda.dtorequest;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.HospitalProjectCholda.data.models.Patient;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest{
    private Patient patient;
    private String doctorEmail;
    private String description;

}
