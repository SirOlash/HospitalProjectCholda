package org.FirstAuctionSystem.data.repositories;

import org.FirstAuctionSystem.data.models.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends MongoRepository<Seller, String> {
}

