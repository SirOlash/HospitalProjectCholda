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
import org.HospitalProjectCholda.services.appointmentservice.AppointmentServices;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//import static java.util.stream.Nodes.collect;


@Service
@AllArgsConstructor
public class DoctorServices implements IDoctorActivities{

    private DoctorRepository doctorRepository;

    private PasswordService passwordService;

    private AppointmentRepository appointmentRepository;

    private AppointmentServices appointmentServices;
    private PatientServices patientServices;

    private PatientRepository patientRepository;


    @Override
    public void updateDoctorDetailedProfile(String id, PatientProfileDetailRequest profile) {

    }

    @Override
    public DoctorResponseDTO createNewDoctor(DoctorRegistrationRequest doctorRequest) throws ConstraintViolationException,  PatientCollectionException {

        Optional<Doctor> foundDoctor = doctorRepository.findByEmail(doctorRequest.getEmail());
        if (foundDoctor.isPresent()){
            throw new DoctorCollectionException(DoctorCollectionException.DoctorAlreadyExists());

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
        Doctor registeredDoctor = new Doctor();
        registeredDoctor.setEmail(doctorRequest.getEmail());
        registeredDoctor.setUserName(doctorRequest.getUserName());
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
            throw new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(email));
        }
        Doctor loggedInDoctor = registeredDoctor.get();

        if (!passwordService.matchesPassword(password, loggedInDoctor.getEncryptedPassword())){
            throw new DoctorCollectionException(DoctorCollectionException.DoctorInvalidEmailOrPassword(loggedInDoctor.getEncryptedPassword()));
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
            throw new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorsId));
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
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorEmail)));
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
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorEmail)));

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
    public Doctor updateDoctorProfile(String currentDoctorId, DoctorRegistrationRequest newDoctorProfile) {
        Doctor foundDoctor = doctorRepository.findById(currentDoctorId)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(currentDoctorId)));

        if (newDoctorProfile.getUserName() != null && !newDoctorProfile.getUserName().trim().isEmpty()) {
            foundDoctor.setUserName(newDoctorProfile.getUserName());
        }
        if (newDoctorProfile.getPassword() != null && !newDoctorProfile.getPassword().trim().isEmpty()) {
            foundDoctor.setEncryptedPassword(passwordService.hashPassword(newDoctorProfile.getPassword()));
        }
        if (newDoctorProfile.getEmail() != null && !newDoctorProfile.getEmail().trim().isEmpty()) {
            if (!newDoctorProfile.getEmail().trim().equals(foundDoctor.getEmail())) {
                doctorRepository.findByEmail(newDoctorProfile.getEmail())
                        .ifPresent(patient -> {
                            throw new DoctorCollectionException(DoctorCollectionException.DoctorAlreadyExists());
                        });
                foundDoctor.setEmail(newDoctorProfile.getEmail());
            }
        }
        doctorRepository.save(foundDoctor);

        return foundDoctor;
    }



    @Override
    public Doctor updateDoctorDetailedProfile(String id, DoctorProfileDetailRequest profile) {
        Doctor foundDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(id)));

        if (foundDoctor.getDoctorProfile() == null){
            foundDoctor.setDoctorProfile(new DoctorProfile());
        }
        DoctorProfile doctorProfile = foundDoctor.getDoctorProfile();

        if (profile.getFirstName() != null) doctorProfile.setFirstName(profile.getFirstName());
        if (profile.getLastName() != null) doctorProfile.setLastName(profile.getLastName());
        if (profile.getAddress() != null) doctorProfile.setAddress(profile.getAddress());
        if (profile.getPhoneNumber() != null) doctorProfile.setPhoneNumber(profile.getPhoneNumber());
        doctorRepository.save(foundDoctor);
        return foundDoctor;
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



}

