package org.HospitalProjectCholda.exceptions;

import java.time.LocalDateTime;

public class AppointmentCollectionException extends RuntimeException{

    public AppointmentCollectionException(String message){
        super(message);
    }
    public static String DoctorIsUnavailable(String doctorEmail, LocalDateTime appointmentTime){
        return "Doctor with email " + doctorEmail + " is not available at this time " + appointmentTime;
    }
}
