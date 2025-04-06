package org.HospitalProjectCholda.dtorequest;


import lombok.Data;

@Data
public class DoctorProfileDetailRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

}
