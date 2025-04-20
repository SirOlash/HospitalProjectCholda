package org.FirstAuctionSystem.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class Seller extends User {
    private String sellerId;

    public Seller(String userName, String email, String encryptedPassword) {
        super(userName, email, encryptedPassword);
    }
}
