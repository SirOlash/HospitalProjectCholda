package org.HospitalProjectCholda.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Setter
@Getter
@Data
public class ErrorResponse {

    private String message;


    private int status;

    private LocalDateTime timestamp;

    public ErrorResponse(String message, LocalDateTime timestamp, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

}
