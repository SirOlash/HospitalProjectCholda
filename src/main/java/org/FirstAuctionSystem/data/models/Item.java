package org.FirstAuctionSystem.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {
    private String itemId;
    private String itemName;
    private String itemDescription;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private Auction auction;
}
