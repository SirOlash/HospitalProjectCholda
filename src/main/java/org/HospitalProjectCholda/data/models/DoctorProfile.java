package org.HospitalProjectCholda.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "doctorProfile")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorProfile extends UserProfile{


    private MaritalStatus maritalStatus;

//    public DoctorProfile(String firstName, String lastName, String phoneNumber, String address) {
//        super(firstName, lastName, phoneNumber, address);
//    }
}
