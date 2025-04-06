package org.HospitalProjectCholda.data.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.HospitalProjectCholda.security.PasswordService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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


}
