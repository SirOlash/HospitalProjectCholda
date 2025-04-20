
package org.FirstAuctionSystem.data.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@TypeAlias("user")
@Data
@AllArgsConstructor
public abstract class User {
    private String userName;

    private String email;
    @NotNull
    private  String encryptedPassword;
}
