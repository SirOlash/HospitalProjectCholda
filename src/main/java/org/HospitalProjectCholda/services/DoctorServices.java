package org.HospitalProjectCholda.services;

import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.exceptions.DuplicateUserException;
import org.HospitalProjectCholda.exceptions.InvalidUserNameOrPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorServices {

    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor createNewDoctor(Doctor doctor){
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()){
            throw new DuplicateUserException("Doctor with email " + doctor.getEmail() + " already exists");
        }
        return doctorRepository.save(doctor);
    }

    public void deleteAll() {
        doctorRepository.deleteAll();
    }

    public long count() {
        return doctorRepository.count();
    }
    public Doctor loginDoctor(String email, String password) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            if (doctor.getPassword().equals(password)) {
                return doctor;
            }
        }
        throw new InvalidUserNameOrPasswordException("Invalid username or password");
    }
}
