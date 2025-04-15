package org.HospitalProjectCholda.dtorequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Patient;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentResponseDTO {

    @Id
    private String appointmentId;
    private  PatientResponseDTO patient;
    private String description;
    private DoctorResponseDTO doctor;
    private LocalDateTime appointmentTime;
    private boolean available = true;

    public AppointmentResponseDTO(Appointment appointment) {
//        this.appointmentId = appointment.getId();
        this.patient = new PatientResponseDTO(appointment.getPatient());
        this.description = appointment.getDescription();
        this.appointmentTime = appointment.getAppointmentTime();
        this.doctor = new DoctorResponseDTO(appointment.getDoctor());
        this.available = appointment.getDoctor().isAvailable();

    }
    public void setDoctorEmail(@NotNull(message = "email cannot be empty!") String email) {
        this.doctor.setEmail(email);
    }


}
