package org.HospitalProjectCholda.exceptions;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException invalidFormatException) {
        String expectedField = invalidFormatException.getPath().get(0).getFieldName();
        String expectedFormat;
        switch(expectedField){
            case "appointmentTime":
                expectedFormat = "yyyy-MM-dd'T'HH:mm:ss";
                break;
            case "dateOfBirth":
                expectedFormat = "yyyy-MM-dd";
                break;
            default:
                expectedFormat = "valid field format";

        }

//        String errorMessage = String.format("'%s' is an invalid format, expected date format is '%s', but got '%s', expectedField, expectedFormat, invalidFormatException.getValue());
        String errorMessage = String.format(
                "Invalid format for field '%s'. Expected format: %s. Provided value: %s",
                expectedField, expectedFormat, invalidFormatException.getValue()
        );
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>  handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errorMessage = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new  ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);


    }

}
