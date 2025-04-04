package org.HospitalProjectCholda.exceptions;

import java.io.Serial;

public class PatientCollectionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PatientCollectionException(String message) {
        super(message);
    }
    public static String PatientNotFoundException(String id) {
        return "Patient with id: " + id + " not found";
    }
    public static String PatientAlreadyExists() {
        return "Patient already exists";
    }
    public static String InvalidEmailOrPassword(String password) {
        return "Invalid email or password";
    }


    public static String PatientWithEmailNotFound(String email) {
        return "Patient with email: " + email + " not found";
    }
}
