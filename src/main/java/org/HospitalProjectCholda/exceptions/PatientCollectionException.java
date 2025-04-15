package org.HospitalProjectCholda.exceptions;

import java.io.Serial;
import java.time.LocalDateTime;

public class PatientCollectionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PatientCollectionException(String message) {

        super(String.valueOf(message));
    }

    public static ErrorResponse PatientNotFoundException(String id) {
        return new ErrorResponse("patient with id " + id + " not found", LocalDateTime.now(), 404);
    }
    public static ErrorResponse PatientAlreadyExists() {

        return new ErrorResponse("Patient already exists", LocalDateTime.now(), 409);
    }
    public static ErrorResponse InvalidEmailOrPassword(String password) {

        return new ErrorResponse("Invalid email or password", LocalDateTime.now(), 401);
    }


    public static ErrorResponse PatientWithEmailNotFound(String email) {
        return new ErrorResponse("patient with email "+ email + " not found", LocalDateTime.now(), 404);
    }
    public static ErrorResponse EmailAlreadyExists(String email) {

        return new ErrorResponse("email " + email + " already exists", LocalDateTime.now(), 409);
    }
}
