package org.HospitalProjectCholda.dtorequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileDetailResponse {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

}
