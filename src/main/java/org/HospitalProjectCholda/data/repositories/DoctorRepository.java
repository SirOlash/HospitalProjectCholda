package org.HospitalProjectCholda.data.repositories;


import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {
    Optional<Doctor> findByEmail(String email);
}

@Override
public Appointment createAppointment(AppointmentRequest request){
    doctorRepository.findByEmail(request.getDoctorEmail()).ifPresent(doctor -> {});
}
