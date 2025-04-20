package org.FirstAuctionSystem.data.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "buyers")
public class Buyer extends User {

    @Id
    private String buyerId;



    public Buyer(String userName, String email, String encryptedPassword) {
        super(userName, email, encryptedPassword);
    }
}
