package org.HospitalProjectCholda.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfile {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

    @Override
    public String toString(){
        String name1 = (getFirstName() != null) ? firstName : "N/A";
        String name2 = (getLastName() != null) ? lastName : "N/A";
        String phone = (getPhoneNumber() != null) ? phoneNumber : "N/A";
        String address1 = (getAddress() != null) ? address : "N/A";
        return "Patient{" +
                "first name=" + name1 +
                ", lastName='" + name2 + '\'' +
                ", phone number='" + phone + '\'' +
                ", address='" + address1 +
        '}';

    }
}
