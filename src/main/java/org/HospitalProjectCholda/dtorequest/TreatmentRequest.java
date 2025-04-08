package org.HospitalProjectCholda.dtorequest;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
@Setter
@Getter
@Data
public class TreatmentRequest {
    @NotNull(message = "treatment description cannot be empty!")
    private String treatment;

    public TreatmentRequest() {}

    public TreatmentRequest(String treatment) {
        this.treatment = treatment;
    }

}
