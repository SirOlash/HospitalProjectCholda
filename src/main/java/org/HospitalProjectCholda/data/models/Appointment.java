package org.HospitalProjectCholda.data.models;

import AppointmentStatus.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@Document(collection = "appointment")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Appointment {
    @Id
    private String id;

    @NotNull(message = "Appointment time cannot be empty!")
    private LocalDateTime appointmentTime;

    @DBRef
    private Patient patient;

    @DBRef
    @Indexed
    private Doctor doctor;

    @NotNull(message =  "Doctor's email cannot be empty!")
//    private String doctorEmail;
    private String description;
    private AppointmentStatus appointmentStatus;
    private LocalDateTime createdAt;


//    private LocalDateTime updatedAt;

}
