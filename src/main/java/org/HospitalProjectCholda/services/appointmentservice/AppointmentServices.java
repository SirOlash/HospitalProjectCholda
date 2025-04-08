package org.HospitalProjectCholda.services.appointmentservice;


import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.dtorequest.AppointmentResponseDTO;
import org.HospitalProjectCholda.dtorequest.TreatmentRequest;
import org.HospitalProjectCholda.exceptions.AppointmentCollectionException;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServices implements IAppointmentActivities{

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequest request) {
        Doctor foundDoctor = doctorRepository.findByEmail(request.getDoctorEmail())
                .orElseThrow(() ->new RuntimeException("Doctor does not exist"));

        if (!foundDoctor.isAvailable()){
            throw new DoctorCollectionException("Doctor with email " + request.getDoctorEmail() + " not available");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(foundDoctor);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setDescription(request.getDescription());
        appointment.setPatient(request.getPatient());
        foundDoctor.setAvailable(false);
        foundDoctor.setHasAcceptedAppointment(false);
        foundDoctor.setCurrentPatientId(request.getPatient().getId());
        doctorRepository.save(foundDoctor);
        appointmentRepository.save(appointment);
        return new AppointmentResponseDTO(appointment);



    }

//    @Override
//    public Appointment completeAppointment(String appointmentId) {
//        Appointment appointments = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new AppointmentCollectionException(appointmentId));
//
//        appointments.setAppointmentStatus(AppointmentStatus.COMPLETED);
//        appointmentRepository.save(appointments);
//
//
//        return appointments;
//    }

    public void deleteAll() {
        appointmentRepository.deleteAll();
    }

    public String getPatientId(String appointmentId) {
        return appointmentRepository.findById(appointmentId).get().getPatient().getId();
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.getDoctorsByAvailable(true);
    }

    public Appointment getAppointmentById(String id) {
        Appointment foundAppointment =  appointmentRepository.findById(id)
                        .orElseThrow(() -> new AppointmentCollectionException("Appointment with id " + id + " not found"));
        return foundAppointment;
    }

    @Override
    public AppointmentResponseDTO bookAppointment(AppointmentRequest appointmentRequest) throws PatientCollectionException {
        patientRepository.findById(appointmentRequest.getPatient().getId())
                .orElseThrow(() -> new PatientCollectionException("Patient not found with id " + appointmentRequest.getPatient().getId()));
        return createAppointment(appointmentRequest);

    }

//    @Override
//    public String completeAppointment(String appointmentId, TreatmentRequest treatmentRequest) {
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new RuntimeException("Appointment not found"));
//
//        Doctor doctor = appointment.getDoctor();
//        Patient patient = appointment.getPatient();
//
//        // Add treatment to patient's medical history
//        patient.addMedicalHistory(
//                LocalDateTime.now(),
//                appointment.getDescription(),
//                treatmentRequest.getTreatment()
//        );
//        patientRepository.save(patient);
//
//        // Update doctor status
//        doctor.setAvailable(true);
//        doctor.setHasAcceptedAppointment(false);
//        doctor.setCurrentPatientId(null);
//        doctorRepository.save(doctor);
//
//        // Optionally, remove the appointment or mark as completed
//        appointmentRepository.delete(appointment); // or set a status field
//
//        return "Appointment completed and treatment recorded.";
//    }
    @Override
    public String completeAppointment(String appointmentId, TreatmentRequest treatmentRequest){
        Appointment scheduledAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment with id " + appointmentId + " not found"));
        Doctor appointedDoctor = scheduledAppointment.getDoctor();
        Patient patient = scheduledAppointment.getPatient();
        patient.addMedicalHistory(
                LocalDateTime.now(),
                scheduledAppointment.getDescription(),
                treatmentRequest.getTreatment()
        );
        patientRepository.save(patient);
        appointedDoctor.setAvailable(true);
        appointedDoctor.setHasAcceptedAppointment(false);
        appointedDoctor.setCurrentPatientId("Appointment with " + patient.getId() + " has been completed\nNo new appointment yet!");
        return "Appointment completed and treatment recorded!";


    }

}
