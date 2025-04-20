package org.FirstAuctionSystem.exceptions;


import java.io.Serial;
import java.time.LocalDateTime;

public class BuyerCollectionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public BuyerCollectionException(String message) {
        super(String.valueOf(message));
    }
    public static ErrorResponse DuplicateEmailException(String email) {
        return new ErrorResponse(LocalDateTime.now(), 409,  "duplicate email", "Buyer already exists with email " + email);
    }

    public static ErrorResponse BuyerEmailNotFoundException(String email) {
        return new ErrorResponse(LocalDateTime.now(), 409, "email not found", "Buyer with email " + email + " not found");
    }

    public static ErrorResponse InvalidEmailOrPasswordException(String password) {
        return new ErrorResponse(LocalDateTime.now(), 401,  "invalid email or password", "Buyer credentials is not valid");
    }
}
