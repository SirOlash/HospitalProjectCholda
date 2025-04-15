package org.HospitalProjectCholda.services.doctorservice;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.HospitalProjectCholda.data.models.*;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.AppointmentCollectionException;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.security.PasswordService;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import static java.util.stream.Nodes.collect;


@Service
@AllArgsConstructor
public class DoctorServices implements IDoctorActivities{
    private DoctorRepository doctorRepository;

    private PasswordService passwordService;

    private AppointmentRepository appointmentRepository;

//    private AppointmentServices appointmentServices;
    private PatientServices doctorProfile;

    private PatientRepository patientRepository;


    @Override
    public void updateDoctorDetailedProfile(String id, PatientProfileDetailRequest profile) {

    }

    @Override
    public DoctorResponseDTO createNewDoctor(DoctorRegistrationRequest doctorRequest) throws ConstraintViolationException,  PatientCollectionException {

        Optional<Doctor> foundDoctor = doctorRepository.findByEmail(doctorRequest.getEmail());
        if (foundDoctor.isPresent()){
            throw new DoctorCollectionException(DoctorCollectionException.DoctorAlreadyExists().getMessage());

        }

        if (doctorRequest.getUserName() == null || doctorRequest.getUserName().trim().isEmpty()) {
            throw new ConstraintViolationException("username cannot be empty!", null);
        }
        if (doctorRequest.getPassword() == null || doctorRequest.getPassword().trim().isEmpty()) {
            throw new ConstraintViolationException("password cannot be empty!", null);
        }
        if (doctorRequest.getEmail() == null || doctorRequest.getEmail().trim().isEmpty()) {
            throw new ConstraintViolationException("email cannot be empty!", null);
        }
        if (doctorRequest.getSpecialty() == null || doctorRequest.getSpecialty().trim().isEmpty()) {
            throw new ConstraintViolationException("specialty cannot be empty!", null);
        }
        Doctor registeredDoctor = new Doctor();
        registeredDoctor.setEmail(doctorRequest.getEmail());
        registeredDoctor.setUserName(doctorRequest.getUserName());
        registeredDoctor.setSpecialty(doctorRequest.getSpecialty());
        registeredDoctor.setAvailable(true);
        registeredDoctor.setEncryptedPassword(passwordService.hashPassword(doctorRequest.getPassword()));


        Doctor savedDoctor = doctorRepository.save(registeredDoctor);


        return new DoctorResponseDTO(savedDoctor);
    }


    @Override
    public long countAllDoctors() {
        return doctorRepository.count();
    }

    @Override
    public void deleteAll() {
        doctorRepository.deleteAll();
    }
    @Override
    public DoctorResponseDTO doctorLogin(String email, String password) {
        Optional<Doctor> registeredDoctor = doctorRepository.findByEmail(email);
        if (registeredDoctor.isEmpty()) {
            throw new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(email).getMessage());
        }
        Doctor loggedInDoctor = registeredDoctor.get();

        if (!passwordService.matchesPassword(password, loggedInDoctor.getEncryptedPassword())){
            throw new DoctorCollectionException(DoctorCollectionException.DoctorInvalidEmailOrPassword(loggedInDoctor.getEncryptedPassword()).getMessage());
        }

        return new DoctorResponseDTO(loggedInDoctor);

    }
    @Override
    public List<Doctor> getAllDoctors() {

        return doctorRepository.findAll();
    }

    @Override
    public Doctor getSpecificDoctor(String doctorsId) throws DoctorCollectionException {
        Optional<Doctor> foundDoctor = doctorRepository.findById(doctorsId);
        if (foundDoctor.isEmpty()) {
            throw new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorsId).getMessage());
        }
        else{
            return foundDoctor.get();
        }
    }

//    @Override
//    public List<Doctor> viewAvailableDoctors() {
//        return List.of();
//    }


//    @Override
//    public List<Doctor> viewAvailableDoctors(){
//        List<Doctor> availableDoctors = doctorRepository.findByAvailableTrue();
//        if (availableDoctors.isEmpty()) {
//            throw new DoctorCollectionException("No doctors available");
//        }
//
//        return availableDoctors;
//    }

    @Override
    public AppointmentResponseDTO viewAppointment(String doctorEmail){
        Doctor doctor = doctorRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorEmail).getMessage()));
        Appointment currentAppointment = appointmentRepository.findAppointmentByDoctor_Id(doctor.getId())
                .orElseThrow(() -> new AppointmentCollectionException(AppointmentCollectionException.NoBookedAppointmentException()));

        return convertToAppointmentResponseDTO(currentAppointment);
    }
    @Override
    public  AppointmentResponseDTO convertToAppointmentResponseDTO(Appointment appointment){
        AppointmentResponseDTO appointmentResponse = new AppointmentResponseDTO(appointment);
        appointmentResponse.setAppointmentId(appointment.getId());
        appointmentResponse.setAppointmentTime(LocalDateTime.from(appointment.getAppointmentTime()));
        appointmentResponse.setDoctorEmail(appointment.getDoctor().getEmail());
        appointmentResponse.setDescription(appointment.getDescription());
        return appointmentResponse;

    }

    public Appointment acceptAppointment(String doctorEmail, String treatment) {
        Doctor doctor = doctorRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorEmail).getMessage()));

        Appointment appointment = appointmentRepository.findAppointmentByDoctor_Id(doctor.getId())
                .orElseThrow(() -> new AppointmentCollectionException(AppointmentCollectionException.NoBookedAppointmentException()));

        Patient patient = appointment.getPatient();
        patient.addMedicalHistory(
                appointment.getAppointmentTime(),
                appointment.getDescription(),
                treatment
        );


        Doctor foundDoctor = appointment.getDoctor();
        foundDoctor.setAvailable(true);

        patientRepository.save(patient);
        doctorRepository.save(foundDoctor);
        return appointmentRepository.save(appointment);
    }
    @Override
    public DoctorUpdateResponse updateDoctorProfile(String currentDoctorId, DoctorRegistrationRequest newDoctorProfile){
        Doctor foundDoctor = doctorRepository.findById(currentDoctorId)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(currentDoctorId).getMessage()));
        DoctorUpdateResponse doctorResponse = new DoctorUpdateResponse();

        String newUserName = newDoctorProfile.getUserName();

        if (newUserName != null && !newUserName.trim().isEmpty()){
            if (newUserName.trim().length() >= 3 && newUserName.trim().length() <= 50){

                if (!newUserName.equals(foundDoctor.getUserName())){
                    foundDoctor.setUserName(newUserName);
                    doctorResponse.getUpdatedFields().put("userName", "[updated]");
                }
                else{
                    doctorResponse.getUnchangedFields().put("userName", "[not updated]");
                }
            }
            else{
                doctorResponse.getFailedFields().put("userName", "Must be between 3 and 50 characters");
                doctorResponse.getUnchangedFields().put("userName", foundDoctor.getUserName());
            }
        }
        else{
            doctorResponse.getUnchangedFields().put("userName", foundDoctor.getUserName());
        }
        String newPassword = newDoctorProfile.getPassword();
        if (newPassword != null && !newPassword.trim().isEmpty()){
            if (newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")){
                foundDoctor.setEncryptedPassword(passwordService.hashPassword(newPassword));
                doctorResponse.getUpdatedFields().put("password", "[updated]");
            }
            else{
                doctorResponse.getFailedFields().put("password", "Password must be at least 8 characters long and contain at least one letter and one number.");
                doctorResponse.getUnchangedFields().put("password", "[Not updated");
            }
        }
        else {
            doctorResponse.getFailedFields().put("password", "Password must be at least 8 characters long and contain at least one letter and one number.");
        }
        String specialty = newDoctorProfile.getSpecialty();
        if (specialty != null && !specialty.trim().isEmpty()) {
            if (!specialty.equals(foundDoctor.getSpecialty())) {
                foundDoctor.setSpecialty(specialty);
                doctorResponse.getUpdatedFields().put("specialty", "[updated]");
            } else {
                doctorResponse.getUnchangedFields().put("specialty", "[not updated]");
            }

        }
        else {
            doctorResponse.getUnchangedFields().put("specialty", "[not updated]");
        }
        String newEmail = newDoctorProfile.getEmail();
        if (newEmail != null && !newEmail.trim().isEmpty()){
            if (newEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                if (!newEmail.equals(foundDoctor.getEmail())){
                    Optional<Doctor> existingDoctor = doctorRepository.findByEmail(newEmail);
                    if (existingDoctor.isPresent()){
                        doctorResponse.getFailedFields().put("email", "Email already in use");
                        doctorResponse.getUnchangedFields().put("email", "Not updated");
                    }
                    else{
                        foundDoctor.setEmail(newEmail);
                        doctorResponse.getUpdatedFields().put("email", "[updated]");
                    }
                }
                else{
                    doctorResponse.getUnchangedFields().put("email", "[Not updated]");
                }
            }
            else{
                doctorResponse.getFailedFields().put("email", "Invalid email format");
                doctorResponse.getUnchangedFields().put("email", "[not updated]");
            }
        }
        else{
            doctorResponse.getUnchangedFields().put("email", "[Not updated]");
        }
        doctorRepository.save(foundDoctor);
        return doctorResponse;
    }

    @Override
    public DoctorProfileResponse viewDoctorProfileById(String patientId) {

        Doctor foundDoctor =  doctorRepository.findById(patientId)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(patientId).getMessage()));

        return new DoctorProfileResponse(
                foundDoctor.getUserName(),
                foundDoctor.getEmail(),
                foundDoctor.getSpecialty(),
                "***********"
        );

    }
    @Override
    public List<AvailableDoctorResponse> getAllAvailableDoctors() {
        return doctorRepository.findByIsAvailableTrue()
                .stream()
                .map(doctor -> new AvailableDoctorResponse(
                        doctor.getUserName(),
                        doctor.getEmail()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public DoctorUpdateResponse updateDoctorDetailedProfile(String doctorId, DoctorProfileDetailRequest profile){
        Doctor foundDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorId).getMessage()));

        if (foundDoctor.getDoctorProfile() == null){
            foundDoctor.setDoctorProfile(new DoctorProfile());
        }
        DoctorProfile doctorProfile = foundDoctor.getDoctorProfile();
        DoctorUpdateResponse doctorUpdateResponse = new DoctorUpdateResponse();

        if (profile.getFirstName() != null && !profile.getFirstName().trim().isEmpty()) {
            doctorProfile.setFirstName(profile.getFirstName());
            doctorUpdateResponse.getUpdatedFields().put("firstName", "[updated]");

        }
        else{
            doctorUpdateResponse.getUnchangedFields().put("firstName", "[not updated]");
        }
        if (profile.getLastName() != null && !profile.getLastName().trim().isEmpty()) {
            doctorProfile.setLastName(profile.getLastName());
            doctorUpdateResponse.getUpdatedFields().put("lastName", "[updated]");
        }
        else{
            doctorUpdateResponse.getUnchangedFields().put("lastName", "[not updated]");
        }
        if (profile.getAddress() != null && !profile.getAddress().trim().isEmpty()) {
            doctorProfile.setAddress(profile.getAddress());
            doctorUpdateResponse.getUpdatedFields().put("address", "[updated]");
        }
        else{
            doctorUpdateResponse.getUnchangedFields().put("address", "[not updated]");
        }

        if (profile.getPhoneNumber() != null && !profile.getPhoneNumber().trim().isEmpty()) {
            String phoneNumber = profile.getPhoneNumber().trim();
            if (phoneNumber.matches("^(\\+234|0)[789][01]\\d{8}$")){
                doctorProfile.setPhoneNumber(phoneNumber);
                doctorUpdateResponse.getUpdatedFields().put("phoneNumber", "[updated]");
            }
            else{
                doctorUpdateResponse.getFailedFields().put("phoneNumber", "Not a valid phone number");
                doctorUpdateResponse.getUnchangedFields().put("phoneNumber", "[not updated]");
            }
        }
        else{
            doctorUpdateResponse.getUnchangedFields().put("phoneNumber", "[not updated]");
        }



        doctorRepository.save(foundDoctor);
        return doctorUpdateResponse;

    }
    @Override
    public DoctorProfileDetailResponse viewDoctorDetailedProfileById(String doctorId) {
        Doctor foundDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorId).getMessage()));

        DoctorProfile doctorProfile = foundDoctor.getDoctorProfile();
        if (doctorProfile == null) {
            throw new DoctorCollectionException("No profile found");
        }


        return new DoctorProfileDetailResponse(
                doctorProfile.getFirstName(),
                doctorProfile.getLastName(),
                doctorProfile.getPhoneNumber(),
                doctorProfile.getAddress()
        );

    }



}

