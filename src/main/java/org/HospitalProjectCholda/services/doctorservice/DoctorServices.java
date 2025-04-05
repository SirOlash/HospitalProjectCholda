package org.HospitalProjectCholda.services.doctorservice;

import AppointmentStatus.AppointmentStatus;
import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.MedicalHistory;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.security.PasswordService;
import org.HospitalProjectCholda.services.appointmentservice.AppointmentServices;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServices implements IDoctorActivities {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentServices appointmentServices;
    @Autowired
    private PatientServices patientServices;
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void createNewDoctor(Doctor doctor) throws ConstraintViolationException,  PatientCollectionException {

        Optional<Doctor> foundDoctor = doctorRepository.findByEmail(doctor.getEmail());
        if (foundDoctor.isPresent()){
            throw new DoctorCollectionException(DoctorCollectionException.DoctorAlreadyExists());

        }

        if (doctor.getUserName() == null || doctor.getUserName().trim().isEmpty()) {
            throw new ConstraintViolationException("username cannot be empty!", null);
        }
        if (doctor.getEncryptedPassword() == null || doctor.getEncryptedPassword().trim().isEmpty()) {
            throw new ConstraintViolationException("password cannot be empty!", null);
        }
        if (doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()) {
            throw new ConstraintViolationException("email cannot be empty!", null);
        }
        doctor.setEncryptedPassword(passwordService.hashPassword(doctor.getEncryptedPassword()));


        doctorRepository.save(doctor);


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
    public Doctor doctorLogin(String email, String password) {
        Optional<Doctor> registeredDoctor = doctorRepository.findByEmail(email);
        if (registeredDoctor.isEmpty()) {
            throw new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(email));
        }
        Doctor loggedInDoctor = registeredDoctor.get();

        if (!passwordService.matchesPassword(password, loggedInDoctor.getEncryptedPassword())){
            throw new DoctorCollectionException(DoctorCollectionException.DoctorInvalidEmailOrPassword(loggedInDoctor.getEncryptedPassword()));
        }

        return loggedInDoctor;

    }

    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> allRegisteredDoctor = doctorRepository.findAll();
        if (allRegisteredDoctor.isEmpty()) {
            return new ArrayList<Doctor>();
        }
        else{
            return allRegisteredDoctor;
        }
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

    @Override
    public void updateDoctor(String id, Doctor doctor) throws DoctorCollectionException {
        Optional<Doctor> foundDoctor = doctorRepository.findById(id);
        Optional<Doctor> foundDoctorWithSameName = doctorRepository.findById(doctor.getId());
        if (foundDoctor.isPresent()) {
            if (foundDoctorWithSameName.isPresent() && !foundDoctorWithSameName.get().equals(doctor)) {
                throw new DoctorCollectionException(DoctorCollectionException.DoctorAlreadyExists());

            }
            Doctor updatedDoctor = foundDoctor.get();
            updatedDoctor.setUserName(doctor.getUserName());
            updatedDoctor.setEmail(doctor.getEmail());
            updatedDoctor.setEncryptedPassword(doctor.getEncryptedPassword());
            updatedDoctor.setDoctorProfile(doctor.getDoctorProfile());
            doctorRepository.save(updatedDoctor);
        }

        else{
                throw new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctor.getId()));
            }

        }

    @Override
    public void updateDoctorAvailability(String doctorsId, boolean isAvailable) throws DoctorCollectionException {
        Doctor availableDoctor = doctorRepository.findById(doctorsId)
                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctorsId)));
        if(isAvailable && hasBeenScheduled(availableDoctor)) throw new IllegalStateException("Cannot set status while still having a pending appointment!");
        availableDoctor.setAvailable(isAvailable);
        doctorRepository.save(availableDoctor);

    }

    @Override
    public boolean hasBeenScheduled(Doctor doctor) {
        return appointmentRepository.existsByDoctorAndAppointmentStatus(doctor, AppointmentStatus.SCHEDULED);
    }

//    @Override
//    public boolean isAppointmentAccepted(Doctor doctor, String appointmentId) {
//        Doctor availableDoctor = doctorRepository.findById(doctor.getId()).orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(doctor.getId())));
//        if (!availableDoctor.isAvailable()){
//            return false;
//        }
//        availableDoctor.setAvailable(false);
//        availableDoctor.setHasAcceptedAppointment(true);
//        availableDoctor.setCurrentPatientId(appointmentServices.getPatientId(appointmentId));
//        doctorRepository.save(availableDoctor);
//        return true;
//    }

//    @Override
//    public void fillMedicalReport(String doctorId, MedicalHistory medicalInfo) throws DoctorCollectionException {
//        Doctor existingDoctor = doctorRepository.findById(doctorId)
//                .orElseThrow(() -> new DoctorCollectionException("Doctor with id: " + doctorId + " not found!"));
//        if (!existingDoctor.isHasAcceptedAppointment()) {
//            throw new IllegalStateException("Doctor must acknowledge appointment by accepting appointment!");
//        }
//        String patientId = existingDoctor.getCurrentPatientId();
//        if (patientId == null) throw new IllegalStateException("Patient not assigned to doctor!");
//
//        MedicalHistory recordEntries = new MedicalHistory(
//                LocalDateTime.now(),
//                medicalInfo.getDescription(),
//                medicalInfo.getTreatment()
//        );
//        updateMedicalHistory(doctorId, patientId, recordEntries);
//
//
//        existingDoctor.setAvailable(true);
//        existingDoctor.setHasAcceptedAppointment(false);
//        existingDoctor.setCurrentPatientId(null);
//        doctorRepository.save(existingDoctor);
//
//    }

    @Override
    public void updateMedicalHistory(String doctorId, String patientId, MedicalHistory medicalInfo) throws DoctorCollectionException {
        Patient foundPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found!"));

        foundPatient.getMedicalHistory().add(medicalInfo);
        patientRepository.save(foundPatient);


    }

    @Override
    public List<Doctor> viewAvailableDoctors(){
        return doctorRepository.getDoctorsByAvailable(true);
    }

}


