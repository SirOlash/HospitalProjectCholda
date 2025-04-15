package org.HospitalProjectCholda.data.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class Patient {
    @Id
    private String id;;

    private PatientProfile patientProfile;

    @NotNull(message = "Username cannot be empty!")
    private String userName;

    @NotNull(message = "email cannot be empty!")
    private String email;

    private List<MedicalHistory> medicalHistory = new ArrayList<>();

    @NotNull
    private  String encryptedPassword;

    public void addMedicalHistory(LocalDateTime recordDate, String description, String treatment) {
        this.medicalHistory.add(new MedicalHistory(recordDate, description, treatment));
    }
    @Override
    public String toString() {
        String profileInfo = (getPatientProfile() != null) ? patientProfile.toString() : "Yet to update profile!";
        String medicalInfo = (getMedicalHistory() != null) ? medicalHistory.toString() : "Yet to update medical history!";
        return "Patient{" +
                "id='" + getId() + '\'' +
                ", profile=" + profileInfo +
                ", userName='" + getUserName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", medicalHistory=" + medicalInfo +
                '}';
    }


}
