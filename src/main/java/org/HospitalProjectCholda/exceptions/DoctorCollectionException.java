package org.HospitalProjectCholda.exceptions;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.time.LocalDateTime;

public class DoctorCollectionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DoctorCollectionException( String message) {
        super(String.valueOf(message));
    }

    public static ErrorResponse DoctorAlreadyExists() {

        return new ErrorResponse("Doctor already exists", LocalDateTime.now(), 409);
    }


    public static ErrorResponse DoctorNotFound(String id) {

        return  new ErrorResponse("Doctor with id: " + id + " not found", LocalDateTime.now(), 404);
    }

    public static ErrorResponse DoctorInvalidEmailOrPassword(String email) {

        return  new ErrorResponse("Invalid email or password", LocalDateTime.now(), 401);
    }
    public static String DoctorNotAvailable(String email) {
        return  "Doctor not available";
    }
    public static String DoctorDoesNotExistsException(){
        return "Doctor does not exists!";
    }
    public static String AppointmentAcceptancePendingException(){
        return "You have not accepted your appointment!";
    }
}