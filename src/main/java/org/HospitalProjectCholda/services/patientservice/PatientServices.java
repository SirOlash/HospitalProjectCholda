package org.HospitalProjectCholda.services.patientservice;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.HospitalProjectCholda.data.models.*;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.security.PasswordService;
import org.HospitalProjectCholda.services.appointmentservice.AppointmentServices;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PatientServices implements IPatientActivities {

    private final PatientRepository patientRepository;


    private PasswordService passwordService;


    private AppointmentServices appointmentServices;


    private AppointmentRepository appointmentRepository;

    private DoctorRepository doctorRepository;



    @Override
    public PatientResponseDTO createNewPatient(PatientRegistrationRequest request)
            throws ConstraintViolationException, PatientCollectionException {

        Optional<Patient> foundPatient = patientRepository.findByEmail(request.getEmail());
        if (foundPatient.isPresent()){
            throw new PatientCollectionException(PatientCollectionException.PatientAlreadyExists());

        }

        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            throw new ConstraintViolationException("username cannot be empty!", null);
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ConstraintViolationException("password cannot be empty!", null);
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ConstraintViolationException("email cannot be empty!", null);
        }
        Patient registeredPatient = new Patient();
        registeredPatient.setEmail(request.getEmail());
        registeredPatient.setUserName(request.getUserName());
        registeredPatient.setEncryptedPassword(passwordService.hashPassword(request.getPassword()));


        Patient patient = patientRepository.save(registeredPatient);

        return new PatientResponseDTO(patient);
    }
    @Override
    public List<Patient> getAllPatients() {

        return patientRepository.findAll();

    }

    @Override
    public Patient getSpecificPatient(String patientId) throws PatientCollectionException {
      Optional<Patient> foundPatient = patientRepository.findById(patientId);
      if (foundPatient.isEmpty()) {
          throw new PatientCollectionException(PatientCollectionException.PatientNotFoundException(patientId));
      }
      else{
          return foundPatient.get();
      }

    }


    @Override
    public long countAllPatients() {
        return patientRepository.count();
    }

    @Override
    public Patient patientLogin(String email, String password) throws PatientCollectionException {
        Optional<Patient> registeredPatient = patientRepository.findByEmail(email);
        if (registeredPatient.isEmpty()){
            throw new PatientCollectionException(PatientCollectionException.PatientWithEmailNotFound(email));
        }
        Patient patient = registeredPatient.get();
        if (!passwordService.matchesPassword(password, patient.getEncryptedPassword())){
            throw new PatientCollectionException(PatientCollectionException.InvalidEmailOrPassword(patient.getEncryptedPassword()));
        }

        return patient;
    }

    @Override
    public Optional<Patient> findByEmail(String email) throws PatientCollectionException {
        return Optional.ofNullable(patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientWithEmailNotFound(email))));
    }

//    @Override
//    public AppointmentResponseDTO bookAppointment(AppointmentRequest appointmentRequest) throws PatientCollectionException {
//        patientRepository.findById(appointmentRequest.getPatient().getId())
//                .orElseThrow(() -> new PatientCollectionException("Patient not found with id " + appointmentRequest.getPatient().getId()));
//        return appointmentServices.createAppointment(appointmentRequest);
//
//    }

    @Override
    public Patient updatePatientProfile(String currentPatientId, PatientRegistrationRequest newPatientProfile) {
        Patient foundPatient = patientRepository.findById(currentPatientId)
                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(currentPatientId)));

        if (newPatientProfile.getUserName() != null && !newPatientProfile.getUserName().trim().isEmpty()) {
            foundPatient.setUserName(newPatientProfile.getUserName());
        }
        if (newPatientProfile.getPassword() != null && !newPatientProfile.getPassword().trim().isEmpty()) {
            foundPatient.setEncryptedPassword(passwordService.hashPassword(newPatientProfile.getPassword()));
        }
        if (newPatientProfile.getEmail() != null && !newPatientProfile.getEmail().trim().isEmpty()) {
            if (!newPatientProfile.getEmail().trim().equals(foundPatient.getEmail())) {
                patientRepository.findByEmail(newPatientProfile.getEmail())
                        .ifPresent(patient -> {
                            throw new PatientCollectionException(PatientCollectionException.EmailAlreadyExists("Email already exists"));
                        });
                foundPatient.setEmail(newPatientProfile.getEmail());
            }
        }
        patientRepository.save(foundPatient);

        return foundPatient;
    }

    @Override
    public void deleteAll() {
        patientRepository.deleteAll();
    }

    @Override
    public Patient updatePatientDetailedProfile(String id, PatientProfileDetailRequest profile) {
        Patient foundPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(id)));

        if (foundPatient.getPatientProfile() == null){
            foundPatient.setPatientProfile(new PatientProfile());
        }
        PatientProfile patientProfile = foundPatient.getPatientProfile();

        if (profile.getFirstName() != null) patientProfile.setFirstName(profile.getFirstName());
        if (profile.getLastName() != null) patientProfile.setLastName(profile.getLastName());
        if (profile.getDateOfBirth() != null) patientProfile.setDateOfBirth(profile.getDateOfBirth());
        if (profile.getAddress() != null) patientProfile.setAddress(profile.getAddress());
        if (profile.getPhoneNumber() != null) patientProfile.setPhoneNumber(profile.getPhoneNumber());
        if (profile.getGender() != null) patientProfile.setGender(profile.getGender());
        patientRepository.save(foundPatient);
        return foundPatient;
    }
    @Override
    public List<MedicalHistory> viewMedicalHistory(String patientId){
        Patient appointedPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(patientId)));
        return appointedPatient.getMedicalHistory();
    }
}

