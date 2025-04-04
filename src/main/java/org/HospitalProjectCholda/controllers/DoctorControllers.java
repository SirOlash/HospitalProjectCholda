package org.HospitalProjectCholda.controllers;

import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.dto.UserLoginRequest;
import org.HospitalProjectCholda.services.DoctorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoctorControllers {

    @Autowired
    private DoctorServices doctorServices;

    @PostMapping("/doctor/register")
    public Doctor registerDoctor(@RequestBody Doctor doctor) {
        return doctorServices.createNewDoctor(doctor);
    }

    @PostMapping("/doctor/login")
    public Doctor loginDoctor(@RequestBody UserLoginRequest request) {
        return doctorServices.loginDoctor(request.getEmail(), request.getPassword());
    }


}
