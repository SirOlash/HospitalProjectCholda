package org.HospitalProjectCholda.data.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.HospitalProjectCholda.security.PasswordService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @DBRef
    private PatientProfile patientProfile;

    @NotNull(message = "Username cannot be empty!")
    private String userName;

    @NotNull(message = "email cannot be empty!")
    private String email;

    @DBRef
    private List<MedicalHistory> medicalHistory;

    @NotNull
    private  String encryptedPassword;


}
