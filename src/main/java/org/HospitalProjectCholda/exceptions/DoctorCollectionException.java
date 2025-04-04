package org.HospitalProjectCholda.exceptions;

import java.io.Serial;

public class DoctorCollectionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DoctorCollectionException(String message) {
        super(message);
    }

    public static String DoctorAlreadyExists() {
        return "Doctor already exists";
    }


    public static String DoctorNotFound(String email) {
        return  "Doctor with email: " + email + " not found";
    }

    public static String DoctorInvalidEmailOrPassword(String email) {
        return  "Invalid email or password";
    }
    public static String DoctorNotAvailable(String email) {
        return  "Doctor not available";
    }
}