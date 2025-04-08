package org.HospitalProjectCholda.data.repositories;


import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

    Optional<Doctor> findByEmail(String email);

    String id(String id);

    List<Doctor> getDoctorsByAvailable(boolean available);

    List<Doctor> findByIsAvailableTrue();
}
