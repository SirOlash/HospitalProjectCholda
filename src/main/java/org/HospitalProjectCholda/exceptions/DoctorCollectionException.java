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


    public static String DoctorNotFound(String id) {
        return  "Doctor with id: " + id + " not found";
    }

    public static String DoctorInvalidEmailOrPassword(String email) {
        return  "Invalid email or password";
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