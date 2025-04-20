package org.FirstAuctionSystem.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bid {
    private String bidId;
    private Buyer buyer;
    private Auction auction;
    private BigDecimal amount;
}
