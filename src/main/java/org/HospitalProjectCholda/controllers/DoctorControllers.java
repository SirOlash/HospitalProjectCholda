package org.HospitalProjectCholda.controllers;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.dtorequest.*;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.ErrorResponse;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.HospitalProjectCholda.services.doctorservice.IDoctorActivities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorControllers {

    @Autowired
    private DoctorServices doctorServices;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private IDoctorActivities iDoctorActivities;

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

        return ResponseEntity.ok(registeredDoctors.toString());

    }
    @GetMapping("/doctor{id}/")
    public  ResponseEntity<?> getSpecificDoctor(@PathVariable(required = false) String id) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body("Doctor Id is required in the URL!");
        }
        try{
            Doctor existingDoctor = doctorServices.getSpecificDoctor(id);

            return new ResponseEntity<>(existingDoctor,HttpStatus.OK);

        }
        catch(DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/doctorLogin")
    public ResponseEntity<?> doctorLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            DoctorResponseDTO existingDoctor = doctorServices.doctorLogin(loginRequest.getEmail(), loginRequest.getPassword());
            String outputMessage = "You have successfully logged in " + existingDoctor.getUserName();
            return new ResponseEntity<>(outputMessage, HttpStatus.OK);
        }
        catch (DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/doctors/{doctorId}/profile")
    public ResponseEntity<?> viewDoctorProfile(@PathVariable String doctorId){
        try {
            DoctorProfileResponse foundDoctor = doctorServices.viewDoctorProfileById(doctorId);
            return new ResponseEntity<>(foundDoctor, HttpStatus.OK);
        }
        catch(DoctorCollectionException e){
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/doctors/{doctorId}/profile")
    public ResponseEntity<?> updateDoctorProfile(@PathVariable String doctorId, @RequestBody DoctorRegistrationRequest doctorRequest) {

        try{
            DoctorUpdateResponse existingPatient = doctorServices.updateDoctorProfile(doctorId, doctorRequest);
            return  new ResponseEntity<>(existingPatient, HttpStatus.OK);
        }
        catch (DoctorCollectionException e){
            ErrorResponse errorResponse1 = new ErrorResponse(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse1, HttpStatus.BAD_REQUEST);
        }
        catch (ConstraintViolationException e){
            ErrorResponse errorResponse2 = new ErrorResponse(e.getMessage(), LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value());
            return new ResponseEntity<>(errorResponse2, HttpStatus.UNPROCESSABLE_ENTITY);

        }

    }

    @PatchMapping("/doctors/{doctorId}/detailed-profile")
    public ResponseEntity<?> updateDoctorDetailedProfile(@PathVariable String doctorId,  @Valid @RequestBody DoctorProfileDetailRequest doctorRequest) {


        try {

            DoctorUpdateResponse existingDoctor = doctorServices.updateDoctorDetailedProfile(doctorId, doctorRequest);

            return new ResponseEntity<>(existingDoctor, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            ErrorResponse errorResponse1 = new ErrorResponse(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<>(errorResponse1, HttpStatus.BAD_REQUEST);
        } catch (DoctorCollectionException e) {
            ErrorResponse errorResponse2 = new ErrorResponse(e.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse2, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/doctors/{doctorId}/detailed-profile")
    public ResponseEntity<?> viewDoctorDetailedProfile(@PathVariable String doctorId) {
        try{
            DoctorProfileDetailResponse foundProfile = doctorServices.viewDoctorDetailedProfileById(doctorId);
            return new ResponseEntity<>(foundProfile, HttpStatus.OK);
        }
        catch(DoctorCollectionException e){
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }


//    @PatchMapping("/doctors/{doctorId}/detailed-profile")
//    public ResponseEntity<?> updateDoctorDetailedProfile(@PathVariable String doctorId,  @RequestBody UpdateDoctorDetailedProfileRequest doctorRequest) {
//        try{
//
//            Doctor existingDoctor = doctorServices.updateDoctorDetailedProfile(doctorId, doctorRequest);
//            String doctorDetails = "Here is the new update on your profile\n" +
//                    "first name: " + existingDoctor.getDoctorProfile().getFirstName() + "\n" +
//                    "last name: " + existingDoctor.getDoctorProfile().getLastName() + "\n" +
//                    "phone number: " + existingDoctor.getDoctorProfile().getPhoneNumber() + "\n" +
//                    "Address: " + existingDoctor.getDoctorProfile().getAddress();
//            return new ResponseEntity<>(doctorDetails, HttpStatus.OK);
//        }
//        catch (ConstraintViolationException e){
//            Doctor returningProfile = doctorRepository.findById(doctorId).orElse(null);
//            if (returningProfile == null){
//                return new ResponseEntity<>("Dcotor not found!", HttpStatus.NOT_FOUND);
//            }
//            DoctorProfile doctorProfile = returningProfile.getDoctorProfile();
//            String doctorDetails;
//            if (doctorProfile == null){
//               doctorDetails =  e.getMessage();
//            }
//            else{
//                doctorDetails = "\n" +
//                        "Invalid Entry: " + e.getMessage() + "\n\n" +
//                        "Here is your current profile:\n" +
//                        "first name: " + returningProfile.getDoctorProfile().getFirstName() + "\n" +
//                        "last name: " + returningProfile.getDoctorProfile().getLastName() + "\n" +
//                        "phone number: " + returningProfile.getDoctorProfile().getPhoneNumber() + "\n" +
//                        "Address: " + returningProfile.getDoctorProfile().getAddress();
//            }
//
//            return new ResponseEntity<>(doctorDetails, HttpStatus.BAD_REQUEST);
//        }
//        catch (DoctorCollectionException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
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
