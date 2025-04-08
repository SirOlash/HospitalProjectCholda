package org.HospitalProjectCholda.controllers;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.dtorequest.AppointmentResponseDTO;
import org.HospitalProjectCholda.dtorequest.AvailableDoctorResponse;
import org.HospitalProjectCholda.dtorequest.TreatmentRequest;
import org.HospitalProjectCholda.exceptions.AppointmentCollectionException;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.services.appointmentservice.AppointmentServices;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentControllers {


    private final AppointmentServices appointmentServices;
    @Autowired
    private DoctorServices doctorServices;

    private TreatmentRequest request;

    @Autowired
    public AppointmentControllers(AppointmentServices appointmentServices) {
        this.appointmentServices = appointmentServices;
    }
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody AppointmentRequest bookingRequest) {

        try {
            if (bookingRequest == null || bookingRequest.getPatient().getId() == null) {
                return ResponseEntity.badRequest().body("Patient ID is required");
            }
            AppointmentResponseDTO bookedAppointment = appointmentServices.bookAppointment(bookingRequest);
            return new ResponseEntity<>(bookedAppointment, HttpStatus.OK);
        }
        catch(PatientCollectionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
    @GetMapping("/{d}")
    public ResponseEntity<?> getAppointmentBYId(@PathVariable String id) {
        try{
            Appointment createdAppointment = appointmentServices.getAppointmentById(id);
            return new ResponseEntity<>(createdAppointment, HttpStatus.OK);
        } catch (AppointmentCollectionException e) {
            return  new ResponseEntity<>(Map.of("Message", e.getMessage()), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableDoctors() {
        List<AvailableDoctorResponse> availableDoctors =  doctorServices.getAllAvailableDoctors();

        return ResponseEntity.ok(availableDoctors);


    }
    @PostMapping("/complete/{appointmentId}")
    public ResponseEntity<?> completeAppointment(@PathVariable String appointmentId,
                                                 @Valid @RequestBody TreatmentRequest treatmentRequest){
        try{
            String appointmentResponse = appointmentServices.completeAppointment(appointmentId, treatmentRequest);
            return ResponseEntity.ok(appointmentResponse);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }


}