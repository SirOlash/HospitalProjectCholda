package org.HospitalProjectCholda.services.doctorservice;

import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.security.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServices implements IDoctorActivities {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordService passwordService;

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

//        if (!loggedInDoctor.getEncryptedPassword().equals(passwordService.hashPassword(password))) {
//            throw new DoctorCollectionException(DoctorCollectionException.DoctorInvalidEmailOrPassword(loggedInDoctor.getEncryptedPassword()));
//        }
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

}


