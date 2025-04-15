package org.HospitalProjectCholda.dtorequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.HospitalProjectCholda.data.models.Gender;
import org.springframework.cglib.core.Local;

import java.net.DatagramPacket;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientProfileDetailResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private Gender gender;




}
