package org.HospitalProjectCholda.services.appointmentservice;


import AppointmentStatus.AppointmentStatus;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.exceptions.AppointmentCollectionException;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
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
    public Appointment createAppointment(AppointmentRequest request) {
        Doctor foundDoctor = doctorRepository.findByEmail(request.getDoctorEmail())
                .orElseThrow(() ->new RuntimeException("Doctor does not exist"));

        if (!foundDoctor.isAvailable()){
            throw new DoctorCollectionException(DoctorCollectionException.DoctorNotFound("Doctor not available"));
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(foundDoctor);
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setDescription(request.getDescription());
        appointment.setPatient(request.getPatient());
        foundDoctor.setAvailable(false);
        doctorRepository.save(foundDoctor);
        return appointmentRepository.save(appointment);



    }

    @Override
    public Appointment completeAppointment(String appointmentId) {
        Appointment appointments = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentCollectionException(appointmentId));

        appointments.setAppointmentStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointments);


        return appointments;
    }

    public void deleteAll() {
        appointmentRepository.deleteAll();
    }

    public String getPatientId(String appointmentId) {
        return appointmentRepository.findById(appointmentId).get().getPatient().getId();
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.getDoctorsByAvailable(true);
    }
}
