package org.HospitalProjectCholda.services.appointmentservice;

import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.dtorequest.AppointmentResponseDTO;
import org.HospitalProjectCholda.dtorequest.TreatmentRequest;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;

import java.util.List;

public interface IAppointmentActivities {
//    public boolean isDoctorAvailable(String doctorEmail, LocalDateTime dateTime);
//    public boolean checkPatientCanBookAppointment(String patientId, String doctorEmail);
//    public Appointment createAppointment(Appointment appointment);
    public AppointmentResponseDTO createAppointment(AppointmentRequest request);

    //    public Appointment completeAppointment(String appointmentId);
    public List<Doctor> getAvailableDoctors();
    public AppointmentResponseDTO bookAppointment(AppointmentRequest appointmentRequest) throws PatientCollectionException;

    String completeAppointment(String appointmentId, TreatmentRequest treatmentRequest);
}
