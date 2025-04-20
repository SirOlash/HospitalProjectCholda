package org.FirstAuctionSystem.data.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.FirstAuctionSystem.data.models.Bid;


@Repository
public interface BidRepository extends MongoRepository<Bid, String> {
}
