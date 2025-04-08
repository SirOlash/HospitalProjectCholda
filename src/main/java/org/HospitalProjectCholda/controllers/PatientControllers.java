package org.HospitalProjectCholda.controllers;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.HospitalProjectCholda.data.models.MedicalHistory;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.services.appointmentservice.AppointmentServices;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientControllers {

    @Autowired
    private PatientServices patientServices;

    @Autowired
    private PatientRepository patientRepository;

    private AppointmentServices appointmentServices;
    @Autowired
    private DoctorServices doctorServices;
    @Autowired
    private DoctorRepository doctorRepository;


    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(
            @Valid @RequestBody PatientRegistrationRequest registrationRequest) {
        try {
            PatientResponseDTO createdPatient = patientServices.createNewPatient(registrationRequest);
            return new ResponseEntity<> (createdPatient, HttpStatus.OK);
        }
        catch(ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(PatientCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/patient")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> allPatients = patientServices.getAllPatients();

        if (allPatients.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("No patient has been registered yet!");
        }
        return ResponseEntity.ok(allPatients);


    }
    @GetMapping("/patient{id}/")
    public ResponseEntity<?> getSpecificPatient(@PathVariable(required = false) String id) {
        if (id == null || id.isBlank()) {
            return  ResponseEntity.badRequest().body("Patient Id is required in the URL!");
        }
        try{
            return new ResponseEntity<>(patientServices.getSpecificPatient(id), HttpStatus.OK);
        }
        catch(PatientCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@Valid @RequestBody LoginRequest login) {
        try{
            Patient existingPatient = patientServices.patientLogin(login.getEmail(), login.getPassword());
            return new ResponseEntity<>(existingPatient, HttpStatus.OK);
        }
        catch (PatientCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @PatchMapping("/patients/{patientId}/profile")
    public ResponseEntity<?> updatePatientProfile(@PathVariable String patientId, @Valid @RequestBody PatientRegistrationRequest patientRequest) {
        try{
            Patient existingPatient = patientServices.updatePatientProfile(patientId, patientRequest);
            return  new ResponseEntity<>(existingPatient, HttpStatus.OK);
        }
        catch (PatientCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

        }

    }
    @PatchMapping("/patients/{patientId}/detailed-profile")
    public ResponseEntity<?> updatePatientDetailedProfile(@PathVariable String patientId,  @Valid @RequestBody PatientProfileDetailRequest patientRequest) {
        try{
            Patient existingPatient = patientServices.updatePatientDetailedProfile(patientId, patientRequest);
            return new ResponseEntity<>(existingPatient, HttpStatus.OK);
        }
        catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (PatientCollectionException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/book-appointment")
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody AppointmentRequest bookingRequest) {
        try {
            AppointmentResponseDTO bookedAppointment = appointmentServices.createAppointment(bookingRequest);
            return new ResponseEntity<>(bookedAppointment, HttpStatus.CREATED);
        }
        catch (
                DoctorCollectionException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred");
        }


    }
    @DeleteMapping("delete-all")
    public ResponseEntity<?> deleteAllPatients(){
        doctorRepository.deleteAll();
        return new ResponseEntity<>("All patients deleted successfully!",  HttpStatus.OK);
    }
    @GetMapping("/history/{patientId}")
    public ResponseEntity<?> getPatientMedicalHistory(@PathVariable String patientId) {
        try{
            List<MedicalHistory> patientHistory = patientServices.viewMedicalHistory(patientId);
            return ResponseEntity.ok(patientHistory);

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    


}