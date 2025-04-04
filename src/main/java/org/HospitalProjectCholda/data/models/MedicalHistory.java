package org.HospitalProjectCholda.data.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Medical History")
public class MedicalHistory {
    @Id
    private String id;

    @NotNull(message = "medical record date cannot be empty!")
    private LocalDateTime recordDate;

    @NotNull(message = "description cannot be empty!")
    private String description;

    private String treatment;

    public MedicalHistory(LocalDateTime recordDate, String description, String treatment) {
        this.recordDate = recordDate;
        this.description = description;
        this.treatment = treatment;
    }


}

