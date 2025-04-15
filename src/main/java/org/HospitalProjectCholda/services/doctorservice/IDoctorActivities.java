
package org.HospitalProjectCholda.services.doctorservice;

import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;

import java.util.List;

public interface IDoctorActivities {






    public long countAllDoctors();

    public void deleteAll();

    public DoctorResponseDTO doctorLogin(String email, String password)  throws DoctorCollectionException;

    public List<Doctor> getAllDoctors();

    public Doctor getSpecificDoctor(String doctorsId) throws DoctorCollectionException;

//    public List<Doctor> viewAvailableDoctors();

    public AppointmentResponseDTO viewAppointment(String doctorEmail);

    public AppointmentResponseDTO convertToAppointmentResponseDTO(Appointment appointment);

    void updateDoctorDetailedProfile(String id, PatientProfileDetailRequest profile);

    public DoctorResponseDTO createNewDoctor(DoctorRegistrationRequest doctorRequest) throws ConstraintViolationException, PatientCollectionException;

    DoctorProfileResponse viewDoctorProfileById(String patientId);

    public List<AvailableDoctorResponse> getAllAvailableDoctors();
    DoctorUpdateResponse updateDoctorProfile(String currentDoctorId, DoctorRegistrationRequest newDoctorProfile);

    DoctorUpdateResponse updateDoctorDetailedProfile(String doctorId, DoctorProfileDetailRequest profile);

    DoctorProfileDetailResponse viewDoctorDetailedProfileById(String doctorId);
}

