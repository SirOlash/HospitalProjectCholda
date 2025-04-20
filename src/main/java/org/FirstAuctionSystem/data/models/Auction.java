package org.FirstAuctionSystem.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Auction {
    private String auctionId;
    private Seller seller;
    private List<Item> items;
    private List<Bid> bids;
    private Payment payment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
