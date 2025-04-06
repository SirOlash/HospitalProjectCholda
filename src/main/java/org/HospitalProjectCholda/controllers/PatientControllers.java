package org.HospitalProjectCholda.controllers;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.LoginRequest;
//import org.HospitalProjectCholda.dtorequest.PatientProfileRequest;
import org.HospitalProjectCholda.dtorequest.PatientRegistrationRequest;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientControllers {

    @Autowired
    private PatientServices patientServices;

    @Autowired
    private PatientRepository patientRepository;

//    @PostMapping("/register")
//    public ResponseEntity<?> registerPatient(@Valid @RequestBody PatientRegistrationRequest request) {
//        try{
//            Patient createdPatient = patientServices.createNewPatient(request);
//            return new  ResponseEntity<Patient>(patient, HttpStatus.OK);
//        }
//        catch(ConstraintViolationException | PatientCollectionException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(
            @Valid @RequestBody PatientRegistrationRequest registrationRequest) {
        try {
            PatientRegistrationRequest createdPatient = patientServices.createNewPatient(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
        } catch (PatientCollectionException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }
    @GetMapping("/patient")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> allPatients = patientServices.getAllPatients();
        return new ResponseEntity<>(allPatients, !allPatients.isEmpty()? HttpStatus.NO_CONTENT : HttpStatus.OK);


    }
    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getSpecificPatient(@PathVariable String id) {
        try{
            return new ResponseEntity<>(patientServices.getSpecificPatient(id), HttpStatus.OK);
        }
        catch(PatientCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
//    @PutMapping("/patient/{id}")
//    public ResponseEntity<?> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
//        try{
//            patientServices.updatePatient(id, patient);
//            return new  ResponseEntity<>("Patient with id " + id + "updated successfully", HttpStatus.OK);
//        }
//        catch(ConstraintViolationException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//        catch(PatientCollectionException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//
//    }
    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest login) {
        try{
            Patient existingPatient = patientServices.patientLogin(login.getEmail(), login.getPassword());
            return new ResponseEntity<>(existingPatient, HttpStatus.OK);
        }
        catch (PatientCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



}