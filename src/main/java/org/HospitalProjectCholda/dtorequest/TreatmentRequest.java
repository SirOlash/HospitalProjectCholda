package org.HospitalProjectCholda.dtorequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
public class TreatmentRequest {
    @NotNull(message = "treatment description cannot be empty!")
    private String treatment;

    public TreatmentRequest(String treatment) {
        this.treatment = treatment;
    }


}
