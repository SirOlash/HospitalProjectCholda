package org.HospitalProjectCholda.services.appointmentservice;

import AppointmentStatus.AppointmentStatus;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;

import java.time.LocalDateTime;

public interface IAppointmentActivities {
//    public boolean isDoctorAvailable(String doctorEmail, LocalDateTime dateTime);
//    public boolean checkPatientCanBookAppointment(String patientId, String doctorEmail);
//    public Appointment createAppointment(Appointment appointment);
    public Appointment createAppointment(AppointmentRequest request);
    public Appointment completeAppointment(String appointmentId);
}
