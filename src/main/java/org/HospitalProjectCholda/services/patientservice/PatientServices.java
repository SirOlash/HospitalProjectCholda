package org.HospitalProjectCholda.services.patientservice;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.HospitalProjectCholda.data.models.*;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.dtorequest.PatientUpdateResponse;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.security.PasswordService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@AllArgsConstructor
public class PatientServices implements IPatientActivities {

    private final PatientRepository patientRepository;


    private PasswordService passwordService;


//    private AppointmentServices appointmentServices;


//    private AppointmentRepository appointmentRepository;

    private DoctorRepository doctorRepository;



    @Override
    public PatientResponseDTO createNewPatient(PatientRegistrationRequest request)
            throws ConstraintViolationException, PatientCollectionException {

        Optional<Patient> foundPatient = patientRepository.findByEmail(request.getEmail());
        if (foundPatient.isPresent()){
            throw new PatientCollectionException(PatientCollectionException.PatientAlreadyExists().getMessage());

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
          throw new PatientCollectionException(PatientCollectionException.PatientNotFoundException(patientId).getMessage());
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
            throw new PatientCollectionException(PatientCollectionException.PatientWithEmailNotFound(email).getMessage());
        }
        Patient patient = registeredPatient.get();
        if (!passwordService.matchesPassword(password, patient.getEncryptedPassword())){
            throw new PatientCollectionException(PatientCollectionException.InvalidEmailOrPassword(patient.getEncryptedPassword()).getMessage());
        }

        return patient;
    }

    @Override
    public Optional<Patient> findByEmail(String email) throws PatientCollectionException {
        return Optional.ofNullable(patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientWithEmailNotFound(email).getMessage())));
    }
    @Override
    public PatientUpdateResponse updatePatientProfile(String currentPatientId, PatientRegistrationRequest newPatientProfile){
       Patient foundPatient = patientRepository.findById(currentPatientId)
               .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(currentPatientId).getMessage()));
       PatientUpdateResponse patientResponse = new PatientUpdateResponse();

       String newUserName = newPatientProfile.getUserName();

       if (newUserName != null && !newUserName.trim().isEmpty()){
           if (newUserName.trim().length() >= 3 && newUserName.trim().length() <= 50){

                if (!newUserName.equals(foundPatient.getUserName())){
                    foundPatient.setUserName(newUserName);
                    patientResponse.getUpdatedFields().put("userName", "[updated]");
                }
                else{
                    patientResponse.getUnchangedFields().put("userName", "[not updated]");
                }
           }
           else{
               patientResponse.getFailedFields().put("userName", "Must be between 3 and 50 characters");
               patientResponse.getUnchangedFields().put("userName", foundPatient.getUserName());
           }
       }
       else{
           patientResponse.getUnchangedFields().put("userName", foundPatient.getUserName());
       }
       String newPassword = newPatientProfile.getPassword();
       if (newPassword != null && !newPassword.trim().isEmpty()){
           if (newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")){
               foundPatient.setEncryptedPassword(passwordService.hashPassword(newPassword));
               patientResponse.getUpdatedFields().put("password", "[updated]");
           }
           else{
               patientResponse.getFailedFields().put("password", "Password must be at least 8 characters long and contain at least one letter and one number.");
               patientResponse.getUnchangedFields().put("password", "[Not updated");
           }
       }
       else{
           patientResponse.getFailedFields().put("password", "Password must be at least 8 characters long and contain at least one letter and one number.");
       }
       String newEmail = newPatientProfile.getEmail();
       if (newEmail != null && !newEmail.trim().isEmpty()){
           if (newEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
               if (!newEmail.equals(foundPatient.getEmail())){
                   Optional<Patient> existingPatient = patientRepository.findByEmail(newEmail);
                   if (existingPatient.isPresent()){
                       patientResponse.getFailedFields().put("email", "Email already in use");
                       patientResponse.getUnchangedFields().put("email", "Not updated");
                   }
                   else{
                       foundPatient.setEmail(newEmail);
                       patientResponse.getUpdatedFields().put("email", "[updated]");
                   }
               }
               else{
                   patientResponse.getUnchangedFields().put("email", "[Not updated]");
               }
           }
           else{
               patientResponse.getFailedFields().put("email", "Invalid email format");
               patientResponse.getUnchangedFields().put("email", "[not updated]");
           }
       }
       else{
           patientResponse.getUnchangedFields().put("email", "[Not updated]");
       }
       patientRepository.save(foundPatient);
       return patientResponse;
    }

    @Override
    public void deleteAll() {
        patientRepository.deleteAll();
    }


    @Override
    public PatientProfileResponse viewPatientProfileById(String patientId) {

      Patient foundPatient =  patientRepository.findById(patientId)
              .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(patientId).getMessage()));

      return new PatientProfileResponse(
              foundPatient.getUserName(),
              foundPatient.getEmail(),
              "***********"
      );

    }
    @Override
    public PatientDetailedProfileResponse viewPatientDetailedProfileById(String patientId) {
       Patient foundPatient = patientRepository.findById(patientId)
               .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(patientId).getMessage()));

       PatientProfile patientProfile = foundPatient.getPatientProfile();
       if (patientProfile == null) {
           throw new PatientCollectionException("No profile found");
       }


       return new PatientDetailedProfileResponse(
               patientProfile.getFirstName(),
               patientProfile.getLastName(),
               patientProfile.getPhoneNumber(),
               patientProfile.getAddress(),
               patientProfile.getDateOfBirth(),
               patientProfile.getGender()
       );

    }
    @Override
    public List<MedicalHistory> viewMedicalHistory(String patientId){
        Patient appointedPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(patientId).getMessage()));
        return appointedPatient.getMedicalHistory();
    }
    @Override
    public PatientUpdateResponse updatePatientDetailedProfile(String patientId, PatientProfileDetailRequest profile){
        Patient foundPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientNotFoundException(patientId).getMessage()));

        if (foundPatient.getPatientProfile() == null){
            foundPatient.setPatientProfile(new PatientProfile());
        }
        PatientProfile patientProfile = foundPatient.getPatientProfile();
        PatientUpdateResponse patientUpdateResponse = new PatientUpdateResponse();

        if (profile.getFirstName() != null && !profile.getFirstName().trim().isEmpty()) {
            patientProfile.setFirstName(profile.getFirstName());
            patientUpdateResponse.getUpdatedFields().put("firstName", "[updated]");

        }
        else{
            patientUpdateResponse.getUnchangedFields().put("firstName", "[not updated]");
        }
        if (profile.getLastName() != null && !profile.getLastName().trim().isEmpty()) {
            patientProfile.setLastName(profile.getLastName());
            patientUpdateResponse.getUpdatedFields().put("lastName", "[updated]");
        }
        else{
            patientUpdateResponse.getUnchangedFields().put("lastName", "[not updated]");
        }
        if (profile.getAddress() != null && !profile.getAddress().trim().isEmpty()) {
            patientProfile.setAddress(profile.getAddress());
            patientUpdateResponse.getUpdatedFields().put("address", "[updated]");
        }
        else{
            patientUpdateResponse.getUnchangedFields().put("address", "[not updated]");
        }
        if (profile.getGender() != null && !profile.getGender().trim().isEmpty()) {
            String gender = profile.getGender().trim().toUpperCase();
            if (gender.equals("MALE") || gender.equals("FEMALE")){
                patientProfile.setGender(Gender.valueOf(gender));
                patientUpdateResponse.getUpdatedFields().put("gender", "[updated]");

            }
            else{
                patientUpdateResponse.getFailedFields().put("gender", "Gender can only be MALE or FEMALE");
                patientUpdateResponse.getUnchangedFields().put("gender", "[not updated]");
            }

        }
        else{
            patientUpdateResponse.getUnchangedFields().put("gender", "[not updated]");
        }
        if (profile.getPhoneNumber() != null && !profile.getPhoneNumber().trim().isEmpty()) {
            String phoneNumber = profile.getPhoneNumber().trim();
            if (phoneNumber.matches("^(\\+234|0)[789][01]\\d{8}$")){
                patientProfile.setPhoneNumber(phoneNumber);
                patientUpdateResponse.getUpdatedFields().put("phoneNumber", "[updated]");
            }
            else{
                patientUpdateResponse.getFailedFields().put("phoneNumber", "Not a valid phone number");
                patientUpdateResponse.getUnchangedFields().put("phoneNumber", "[not updated]");
            }
        }
        else{
            patientUpdateResponse.getUnchangedFields().put("phoneNumber", "[not updated]");
        }

        if (profile.getDateOfBirth() != null && !profile.getDateOfBirth().trim().isEmpty()) {
            try{
               LocalDate dateOfBirth = LocalDate.parse(profile.getDateOfBirth());
               if (dateOfBirth.isAfter(LocalDate.now())){
                   patientUpdateResponse.getFailedFields().put("dateOfBirth", "Date of birth cannot be in the future");
                   patientUpdateResponse.getUnchangedFields().put("dateOfBirth", "[not updated]");

               }
               else{
                   patientProfile.setDateOfBirth(dateOfBirth);
                   patientUpdateResponse.getUpdatedFields().put("dateOfBirth", "[updated]");
               }
            }
            catch(DateTimeParseException e){
                patientUpdateResponse.getFailedFields().put("dateOfBirth", "Date must be in the format yyyy-MM-dd");
                patientUpdateResponse.getUnchangedFields().put("dateOfBirth", "[not updated]");
            }
        }
        else{
            patientUpdateResponse.getUnchangedFields().put("dateOfBirth", "[not updated]");
        }

        patientRepository.save(foundPatient);
        return patientUpdateResponse;

    }
}

