package org.HospitalProjectCholda.data.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.DatagramPacket;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@Document(collection = "doctor")
public class Doctor {
    @Id
    private String id;

    private DoctorProfile doctorProfile;

    @NotNull(message = "username cannot be empty!")
    private String userName;

    @NotNull(message = "email cannot be empty!")
    private String email;

    @Setter
    private boolean isAvailable = true;

    @NotNull(message = "password cannot be empty!")
    private String encryptedPassword;

    private boolean hasAcceptedAppointment;

    private String specialty;

    private String currentPatientId;


}


