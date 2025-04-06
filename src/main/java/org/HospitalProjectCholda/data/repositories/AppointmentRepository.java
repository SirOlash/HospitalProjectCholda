package org.HospitalProjectCholda.data.repositories;

import AppointmentStatus.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByDoctorEmailAndAppointmentTime(@NotNull(message =  "Doctor's email cannot be empty!") String doctorEmail, @NotNull(message = "Appointment time cannot be empty!") LocalDateTime appointmentTime);

    List<Appointment> findByPatientIdAndDoctorEmailAndAppointmentStatus(String patientId, @NotNull(message =  "Doctor's email cannot be empty!") String doctorEmail, AppointmentStatus appointmentStatus);

    boolean existsByDoctorAndAppointmentStatus(Doctor doctor, AppointmentStatus appointmentStatus);

    @Query("{ 'doctor.email': ?0 }")
    Optional<Appointment> findAppointmentByDoctor_Email(@NotNull(message = "email cannot be empty!") String email);


    Optional<Appointment> findAppointmentByDoctor_Id(String id);
}
