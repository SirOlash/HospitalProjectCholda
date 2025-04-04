package org.HospitalProjectCholda.data.repositories;

import AppointmentStatus.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByDoctorEmailAndAppointmentTime(@NotNull(message =  "Doctor's email cannot be empty!") String doctorEmail, @NotNull(message = "Appointment time cannot be empty!") LocalDateTime appointmentTime);

    List<Appointment> findByPatientIdAndDoctorEmailAndAppointmentStatus(String patientId, @NotNull(message =  "Doctor's email cannot be empty!") String doctorEmail, AppointmentStatus appointmentStatus);

    boolean existsByDoctorAndAppointmentStatus(Doctor doctor, AppointmentStatus appointmentStatus);

//    AppointmentStatus.AppointmentStatus appointmentStatus(AppointmentStatus.AppointmentStatus appointmentStatus);
}
