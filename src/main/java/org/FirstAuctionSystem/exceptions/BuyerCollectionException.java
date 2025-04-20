package org.FirstAuctionSystem.exceptions;


import java.io.Serial;
import java.time.LocalDateTime;

public class BuyerCollectionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public BuyerCollectionException(String message) {
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
    }
}
