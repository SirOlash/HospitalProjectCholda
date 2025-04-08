package org.HospitalProjectCholda.dtorequest;

import lombok.Data;
import lombok.Getter;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.DoctorProfile;

@Data
public class DoctorResponseDTO {

    private String email;
    private String fullName;
    private String userName;
    private boolean available = true;
    private String id;

    public DoctorResponseDTO(Doctor doctor) {
        this.email = doctor.getEmail();

        this.userName = doctor.getUserName();
        if (doctor.getDoctorProfile() != null) {

            this.fullName = doctor.getDoctorProfile().getFirstName() + " " + doctor.getDoctorProfile().getLastName();
        }
        else{
            this.fullName = "No name filled in profile yet";
        }
//        this.available = doctor.isAvailable();
        this.available = doctor.isAvailable();
        this.id = doctor.getId();
    }


    public void setDoctorProfile(DoctorProfile doctorProfile) {
        this.fullName = doctorProfile.getFirstName() + " " + doctorProfile.getLastName();
    }
}
