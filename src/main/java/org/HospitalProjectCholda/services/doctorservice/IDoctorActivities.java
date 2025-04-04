package org.HospitalProjectCholda.services.doctorservice;

import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.MedicalHistory;
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
    public boolean isAppointmentAccepted(Doctor doctor, String appointmentId);
    public void fillMedicalReport(String doctorsId, MedicalHistory medicalInfo)  throws DoctorCollectionException;
    public void updateMedicalHistory(String doctorsId, String patientsId, MedicalHistory medicalInfo) throws DoctorCollectionException;
}
