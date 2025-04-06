
package org.HospitalProjectCholda.services.doctorservice;

import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;

import java.util.List;

public interface IDoctorActivities {

    //    @Override
    //    public void updateDoctorDetailedProfile(String id, DoctorProfileDetailRequest doctorProfile) {
    //        Doctor foundDoctor = doctorRepository.findById(id)
    //                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(id)));
    //
    //        if (foundDoctor.getDoctorProfile() == null){
    //            foundDoctor.setDoctorProfile(new DoctorProfile());
    //        }
    //        DoctorProfile updatedDoctor = foundDoctor.getDoctorProfile();
    //
    //        if (doctorProfile.getFirstName() != null) updatedDoctor.setFirstName(doctorProfile.getFirstName());
    //        if (doctorProfile.getLastName() != null) updatedDoctor.setLastName(doctorProfile.getLastName());
    //        if (doctorProfile.getAddress() != null) updatedDoctor.setAddress(doctorProfile.getAddress());
    //        if (doctorProfile.getPhoneNumber() != null) updatedDoctor.setPhoneNumber(doctorProfile.getPhoneNumber());
    //        doctorRepository.save(foundDoctor);
    //    }
    void updateDoctorDetailedProfile(String id, PatientProfileDetailRequest profile);

    public void createNewDoctor(DoctorRegistrationRequest doctorRequest) throws ConstraintViolationException, DoctorCollectionException;

    public long countAllDoctors();

    public void deleteAll();

    public Doctor doctorLogin(String email, String password) throws DoctorCollectionException;

    public List<Doctor> getAllDoctors();

    public Doctor getSpecificDoctor(String doctorsId) throws DoctorCollectionException;

    public List<Doctor> viewAvailableDoctors();

    public AppointmentResponseDTO viewAppointment(String doctorEmail);

    public AppointmentResponseDTO convertToAppointmentResponseDTO(Appointment appointment);

    public void updateDoctorDetailedProfile(String id, DoctorProfileDetailRequest profile);

    public void updateDoctorProfile(String currentDoctorId, DoctorRegistrationRequest newDoctorProfile);
}

