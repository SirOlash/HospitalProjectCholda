package org.FirstAuctionSystem.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {

    private String paymentId;
    private Auction auction;
    private BigDecimal currentPrice;
}
