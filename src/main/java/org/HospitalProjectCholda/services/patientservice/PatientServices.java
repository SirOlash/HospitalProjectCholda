package org.HospitalProjectCholda.services.patientservice;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.security.PasswordService;
import org.HospitalProjectCholda.services.appointmentservice.AppointmentServices;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PatientServices implements IPatientActivities {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private AppointmentServices appointmentServices;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public void createNewPatient(Patient patient) throws ConstraintViolationException, PatientCollectionException {
        //check if it exists first
        Optional<Patient> foundPatient = patientRepository.findByEmail(patient.getEmail());
        if (foundPatient.isPresent()){
            throw new PatientCollectionException(PatientCollectionException.PatientAlreadyExists());

        }

        if (patient.getUserName() == null || patient.getUserName().isEmpty()) {
                throw new ConstraintViolationException("username cannot be empty!", null);
        }
        if (patient.getEncryptedPassword() == null || patient.getEncryptedPassword().isEmpty()) {
            throw new ConstraintViolationException("password cannot be empty!", null);
        }
        if (patient.getEmail() == null || patient.getEmail().isEmpty()) {
            throw new ConstraintViolationException("email cannot be empty!", null);
        }
        if (patient.getMedicalHistory() == null) {
            patient.setMedicalHistory(new ArrayList<>());
        }
        patient.setEncryptedPassword(passwordService.hashPassword(patient.getEncryptedPassword()));
        patientRepository.save(patient);

    }

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> allPatient = patientRepository.findAll();
        if (!allPatient.isEmpty()) {
            return new  ArrayList<Patient>();
        }
        else{
            return allPatient;
        }

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
    public void updatePatient(String id, Patient patient) throws PatientCollectionException {
        Optional<Patient> foundPatient = patientRepository.findById(id);
        Optional<Patient> foundPatientWithSameName = patientRepository.findByEmail(patient.getEmail());
        if (foundPatient.isPresent()){
            if (foundPatientWithSameName.isPresent() && !foundPatientWithSameName.get().equals(patient)){
                throw new PatientCollectionException(PatientCollectionException.PatientAlreadyExists());

            }
            Patient updatedPatient = foundPatient.get();
            updatedPatient.setPatientProfile(patient.getPatientProfile());
            updatedPatient.setUserName(patient.getUserName());
            updatedPatient.setEmail(patient.getEmail());

            if (patient.getEncryptedPassword() != null && !patient.getEncryptedPassword().isEmpty()) {
                updatedPatient.setEncryptedPassword(passwordService.hashPassword(patient.getEncryptedPassword()));
            }
            patientRepository.save(updatedPatient);

        }
        else{
            throw new PatientCollectionException(PatientCollectionException.PatientNotFoundException(patient.getId()));
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

    @Override
    public Appointment bookAppointment(AppointmentRequest appointmentRequest) throws PatientCollectionException {
        Patient existingPatient = patientRepository.findById(appointmentRequest.getPatient().getId())
                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(appointmentRequest.getPatient().getId())));
        return appointmentServices.createAppointment(appointmentRequest);

    }

    @Override
    public void deleteAll() {
        patientRepository.deleteAll();
    }



}

