
package org.HospitalProjectCholda.services.doctorservice;

import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDoctorActivities {


    void updateDoctorDetailedProfile(String id, PatientProfileDetailRequest profile);



    public long countAllDoctors();

    public void deleteAll();

    public DoctorResponseDTO doctorLogin(String email, String password)  throws DoctorCollectionException;

    public List<Doctor> getAllDoctors();

    public Doctor getSpecificDoctor(String doctorsId) throws DoctorCollectionException;

//    public List<Doctor> viewAvailableDoctors();

    public AppointmentResponseDTO viewAppointment(String doctorEmail);

    public AppointmentResponseDTO convertToAppointmentResponseDTO(Appointment appointment);

    public Doctor updateDoctorDetailedProfile(String id, DoctorProfileDetailRequest profile);

    public Doctor updateDoctorProfile(String currentDoctorId, DoctorRegistrationRequest newDoctorProfile);
    public DoctorResponseDTO createNewDoctor(DoctorRegistrationRequest doctorRequest) throws ConstraintViolationException, PatientCollectionException;
    public List<AvailableDoctorResponse> getAllAvailableDoctors();
}

