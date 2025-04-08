package org.HospitalProjectCholda.controllers;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorControllers {

    @Autowired
    private DoctorServices doctorServices;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody DoctorRegistrationRequest doctorRequest) {
        try{
           DoctorResponseDTO doctorResponseDTO = doctorServices.createNewDoctor(doctorRequest);
            return new ResponseEntity<> (doctorResponseDTO,HttpStatus.OK);
        }
        catch(ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }

    }
    @GetMapping("/doctor/")
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> registeredDoctors = doctorServices.getAllDoctors();
        if (registeredDoctors.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No doctor has been registered yet!");
        }
        return ResponseEntity.ok(registeredDoctors);

    }
    @GetMapping("/doctor{id}/")
    public  ResponseEntity<?> getSpecificDoctor(@PathVariable(required = false) String id) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body("Doctor Id is required in the URL!");
        }
        try{
            return new ResponseEntity<>(doctorServices.getSpecificDoctor(id),HttpStatus.OK);

        }
        catch(DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/doctorLogin")
    public ResponseEntity<?> doctorLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            DoctorResponseDTO existingDoctor = doctorServices.doctorLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(existingDoctor, HttpStatus.OK);
        }
        catch (DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/doctors/{doctorId}/profile")
    public ResponseEntity<?> updateDoctorProfile(@PathVariable String doctorId, @Valid @RequestBody DoctorRegistrationRequest doctorRequest) {
        try{
            Doctor existingDoctor =  doctorServices.updateDoctorProfile(doctorId, doctorRequest);
            return new ResponseEntity<>(existingDoctor, HttpStatus.OK);
        }
        catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
    @PatchMapping("/doctors/{doctorId}/detailed-profile")
    public ResponseEntity<?> updateDoctorDetailedProfile(@PathVariable String doctorId,  @Valid @RequestBody DoctorProfileDetailRequest doctorRequest) {
        try{
            Doctor existingDoctor = doctorServices.updateDoctorDetailedProfile(doctorId, doctorRequest);
            return new ResponseEntity<>(existingDoctor, HttpStatus.OK);
        }
        catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (DoctorCollectionException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("delete-all")
    public ResponseEntity<?> deleteAllDoctors() {
        doctorServices.deleteAll();
        return new ResponseEntity<>("All doctors delete successfully!", HttpStatus.OK);
    }
    @GetMapping("/patient")
    public ResponseEntity<?> missingIdHandler() {
        return ResponseEntity.badRequest().body("Missing patientId. Provide an Id in the format /patients/patient/{id}/");
    }
    @PutMapping("/appointments/accept/{doctorId}")
    public ResponseEntity<?> acceptAppointment(@PathVariable String doctorId){
        Doctor foundDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found!"));

        foundDoctor.setHasAcceptedAppointment(true);
        doctorRepository.save(foundDoctor);

        return ResponseEntity.ok("Appointment has been accepted!");

    }




}
