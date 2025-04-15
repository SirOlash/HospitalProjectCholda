package org.HospitalProjectCholda.dtorequest;

import lombok.Data;
import lombok.Getter;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.DoctorProfile;
import org.springframework.data.annotation.Id;

@Data
public class DoctorResponseDTO {

    @Getter
    private String email;
    private String fullName;
    private String userName;
    private String specialty;
    private boolean available = true;
    private String id;

    public DoctorResponseDTO(Doctor doctor) {
        this.email = doctor.getEmail();

        this.userName = doctor.getUserName();

        this.specialty = doctor.getSpecialty();
        if (doctor.getDoctorProfile() != null) {

            this.fullName = doctor.getDoctorProfile().getFirstName() + " " + doctor.getDoctorProfile().getLastName();
        }
        else{
            this.fullName = "No name filled in profile yet";
        }


        this.available = doctor.isAvailable();
        this.id = doctor.getId();
    }


    public void setDoctorProfile(DoctorProfile doctorProfile) {
        this.fullName = doctorProfile.getFirstName() + " " + doctorProfile.getLastName();
    }

}
