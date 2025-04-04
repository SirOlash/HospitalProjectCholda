package org.HospitalProjectCholda.controllers;


import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.dtorequest.LoginRequest;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctors")
public class DoctorControllers {

    @Autowired
    private DoctorServices doctorServices;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@RequestBody Doctor doctor) {
        try{
            doctorServices.createNewDoctor(doctor);
            return new ResponseEntity<Doctor> (doctor,HttpStatus.OK);
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
        return new ResponseEntity<>(registeredDoctors, !registeredDoctors.isEmpty()? HttpStatus.NO_CONTENT: HttpStatus.OK);

    }
    @GetMapping("/doctor{id}/")
    public  ResponseEntity<?> getSpecificDoctor(@PathVariable String id) {
        try{
            return new ResponseEntity<>(doctorServices.getSpecificDoctor(id),HttpStatus.OK);

        }
        catch(DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }

    }
    @PutMapping("/doctor{doctorsId}/")
    public ResponseEntity<?> updateDoctor(@PathVariable String doctorsId, @RequestBody Doctor doctor) {
        try{
            doctorServices.updateDoctor(doctorsId,doctor);
            return new ResponseEntity<>("Doctor with id " + doctorsId + " updated successfully", HttpStatus.OK);
        }
        catch(ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch(DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/doctorLogin")
    public ResponseEntity<?> doctorLogin(@RequestBody LoginRequest loginRequest) {
        try{
            Doctor existingDoctor = doctorServices.doctorLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(existingDoctor, HttpStatus.OK);
        }
        catch (DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }




}
