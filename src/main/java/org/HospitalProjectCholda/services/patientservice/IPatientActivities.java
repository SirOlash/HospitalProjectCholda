package org.HospitalProjectCholda.services.patientservice;

import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;

import java.util.List;
import java.util.Optional;

public interface IPatientActivities {
    public PatientRegistrationRequest createNewPatient(PatientRegistrationRequest request) throws ConstraintViolationException, PatientCollectionException;
    public List<Patient> getAllPatients();
    public Patient getSpecificPatient(String patientId) throws  PatientCollectionException;
//    public void updatePatient(String Id, Patient patient) throws PatientCollectionException;
    public void deleteAll();
    public long countAllPatients();
    public Patient patientLogin(String email, String password) throws PatientCollectionException;
    public Optional<Patient> findByEmail(String email) throws PatientCollectionException;
    public AppointmentResponseDTO bookAppointment(AppointmentRequest appointmentRequest) throws PatientCollectionException;
    public void updatePatientProfile(String currentPatientId, PatientRegistrationRequest newPatientProfile);

    void updatePatientDetailedProfile(String id, PatientProfileDetailRequest profile);
}