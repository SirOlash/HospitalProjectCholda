

package org.FirstAuctionSystem.data.repositories;

import org.FirstAuctionSystem.data.models.Auction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends MongoRepository<Auction, String> {
}

