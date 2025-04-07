package org.HospitalProjectCholda.controllers;


import jakarta.validation.ConstraintViolationException;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.dtorequest.DoctorRegistrationRequest;
import org.HospitalProjectCholda.dtorequest.DoctorResponseDTO;
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
    public ResponseEntity<?> registerDoctor(@RequestBody DoctorRegistrationRequest doctorRequest) {
        try{
            doctorServices.createNewDoctor(doctorRequest);
            return new ResponseEntity<DoctorRegistrationRequest> (doctorRequest,HttpStatus.OK);
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

    @PostMapping("/doctorLogin")
    public ResponseEntity<?> doctorLogin(@RequestBody LoginRequest loginRequest) {
        try{
            DoctorResponseDTO existingDoctor = doctorServices.doctorLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(existingDoctor, HttpStatus.OK);
        }
        catch (DoctorCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
