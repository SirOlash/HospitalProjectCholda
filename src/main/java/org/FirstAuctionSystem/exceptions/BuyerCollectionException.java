package org.FirstAuctionSystem.exceptions;


import java.io.Serial;
import java.time.LocalDateTime;

public class BuyerCollectionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public BuyerCollectionException(String message) {
<<<<<<< HEAD
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
=======
        super(message);
    }
    public static String DuplicateEmailException(String email) {
        return "duplicate email, Buyer with email: " + email + " already exists";
    }

    public static String BuyerEmailNotFoundException(String email) {
        return "Buyer with email " + email + " not found");
    }

    public static String InvalidEmailOrPasswordException(String password) {
        return "Invalid email or password Buyer credentials is not valid";
>>>>>>> bd596cf8b0ad109be1241288dc99081f3c298985
    }
}
