package org.HospitalProjectCholda.exceptions;

public class InvalidUserNameOrPasswordException extends RuntimeException{
    public InvalidUserNameOrPasswordException(String message) {
        super(message);
    }
}
