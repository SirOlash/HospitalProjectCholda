package org.HospitalProjectCholda.services.appointmentservice;


import AppointmentStatus.AppointmentStatus;
import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.exceptions.AppointmentCollectionException;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.NoAppointmentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServices implements IAppointmentActivities{

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Appointment createAppointment(AppointmentRequest request) {
        Doctor foundDoctor = doctorRepository.findByEmail(request.getDoctorEmail())
                .orElseThrow(() ->new RuntimeException("Doctor not found"));

        if (!foundDoctor.isAvailable()){
            throw new DoctorCollectionException(DoctorCollectionException.DoctorNotFound("Doctor not found"));
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(foundDoctor);
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setDescription(request.getDescription());
        appointment.setPatient(request.getPatient());
        foundDoctor.setAvailable(false);
        return appointmentRepository.save(appointment);


//    @Override
//    public boolean isDoctorAvailable(String doctorEmail, LocalDateTime appointmentTime) {
//        List<Appointment> appointments = appointmentRepository.findByDoctorEmailAndAppointmentTime(doctorEmail, appointmentTime);
//        return appointments.isEmpty();
//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    }
//
//    @Override
//    public boolean checkPatientCanBookAppointment(String patientId, String doctorEmail) {
//        List<Appointment> appointments = appointmentRepository.findByPatientIdAndDoctorEmailAndAppointmentStatus(patientId, doctorEmail, AppointmentStatus.SCHEDULED);
//        return appointments.isEmpty();
//
//    }
//
//    @Override
//    public Appointment createAppointment(Appointment appointment) {
//        if (!isDoctorAvailable(appointment.getDoctorEmail(), appointment.getAppointmentTime())) {
//            throw new AppointmentCollectionException(AppointmentCollectionException.DoctorIsUnavailable(appointment.getDoctorEmail(), appointment.getAppointmentTime()));
//        }
//        if (!checkPatientCanBookAppointment(appointment.getPatient().getId(), appointment.getDoctorEmail())) {
//            throw new ConstraintViolationException("Patient has already scheduled an appointment with doctor!", null);
//        }
//        appointment.setCreatedAt(LocalDateTime.now());
//        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
//        return appointmentRepository.save(appointment);
//    }
//
    }


    @Override
    public Appointment completeAppointment(String appointmentId) {
        Appointment appointments = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentCollectionException(appointmentId));

        appointments.setAppointmentStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointments);

//        notificationService.sendToDoctor(appointments.getDoctor().getId(), "Appointment completed. Update your availability!");

        return appointments;
    }


    public void deleteAll() {
        appointmentRepository.deleteAll();
    }
}
