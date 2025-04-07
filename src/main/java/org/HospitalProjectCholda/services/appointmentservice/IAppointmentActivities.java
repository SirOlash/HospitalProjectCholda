package org.HospitalProjectCholda.services.appointmentservice;

import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.dtorequest.AppointmentResponseDTO;

import java.util.List;

public interface IAppointmentActivities {
//    public boolean isDoctorAvailable(String doctorEmail, LocalDateTime dateTime);
//    public boolean checkPatientCanBookAppointment(String patientId, String doctorEmail);
//    public Appointment createAppointment(Appointment appointment);
    public AppointmentResponseDTO createAppointment(AppointmentRequest request);
    public Appointment completeAppointment(String appointmentId);
    public List<Doctor> getAvailableDoctors();
}
