package org.HospitalProjectCholda.dtorequest;

import lombok.Data;
import lombok.Getter;
import org.HospitalProjectCholda.data.models.Patient;
import org.springframework.data.annotation.Id;

@Data
public class PatientResponseDTO {
    @Getter
    private String email;
    private String fullName;
    private String userName;
    private String id;

    public PatientResponseDTO(Patient patient) {
        this.email = patient.getEmail();
        this.userName = patient.getUserName();

        if (patient.getPatientProfile() != null) {
            this.fullName = patient.getPatientProfile().getFirstName() + " " + patient.getPatientProfile().getLastName();
        }
        else{
            this.fullName = "No name in profile yet";
        }
        this.id = patient.getId();
    }

}
