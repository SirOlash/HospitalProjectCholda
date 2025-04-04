package org.HospitalProjectCholda.data.repositories;

import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.models.PatientProfile;
import org.apache.catalina.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    Optional<Patient> findByPatientProfile(PatientProfile patientProfile);

    Optional<Patient> findByEmail(String email);
    Optional<Patient> findById(String id);


}

