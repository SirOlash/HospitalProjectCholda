package org.HospitalProjectCholda.data.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "doctorProfile")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorProfile extends UserProfile{

    private MaritalStatus maritalStatus;


}
