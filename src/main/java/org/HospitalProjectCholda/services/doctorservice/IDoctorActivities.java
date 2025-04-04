package org.HospitalProjectCholda.services.doctorservice;

import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;

import java.util.List;

public interface IDoctorActivities {
    public void createNewDoctor(Doctor doctor) throws ConstraintViolationException, DoctorCollectionException;
    public long countAllDoctors();
    public void deleteAll();
    public Doctor doctorLogin(String email, String password) throws DoctorCollectionException;
    public List<Doctor> getAllDoctors();
    public Doctor getSpecificDoctor(String doctorsId) throws DoctorCollectionException;
    public void updateDoctor(String doctorsId, Doctor doctor) throws DoctorCollectionException;
    public void updateDoctorAvailability(String doctorsId, boolean available) throws DoctorCollectionException;
    public boolean hasBeenScheduled(Doctor doctor);
}
