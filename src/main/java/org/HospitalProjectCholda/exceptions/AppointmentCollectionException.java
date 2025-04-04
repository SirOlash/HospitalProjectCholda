package org.HospitalProjectCholda.exceptions;

import java.time.LocalDateTime;

public class AppointmentCollectionException extends RuntimeException{

    public AppointmentCollectionException(String message){
        super(message);
    }
    public static String DoctorIsUnavailable(String doctorEmail, LocalDateTime appointmentTime){
        return "Doctor with email " + doctorEmail + " is not available at this time " + appointmentTime;
    }
    public  static String AppointmentNotFoundException(String doctorId, LocalDateTime appointmentTime){
        return "Appointment not found!";
    }

    public static String NoBookedAppointmentException(){
        return "You have not been booked for an appointment";
    }
}
