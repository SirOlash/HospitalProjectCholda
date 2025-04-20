package org.FirstAuctionSystem.data.dtorequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.FirstAuctionSystem.data.models.Buyer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerResponse {
    private String message;
    private String userName;
    private String email;


    public BuyerResponse(Buyer buyer) {
        this.email = buyer.getEmail();
        this.userName = buyer.getUserName();
        this.message = "welcome " + userName;

    }
}
