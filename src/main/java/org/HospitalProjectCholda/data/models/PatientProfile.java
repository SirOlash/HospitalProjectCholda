package org.HospitalProjectCholda.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "patientProfile")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientProfile extends UserProfile{

    @Id
    private String id;
    private LocalDate dateOfBirth;
    private Gender gender;

    @Override
    public String toString(){
        String birthday = (getDateOfBirth() != null) ? getDateOfBirth().toString() : "N/A";
        String gender1 = (getGender() != null) ? getGender().toString() : "N/A";
        return super.toString() + ", date of birth=" + birthday + ", gender=" + gender1;
    }


}
