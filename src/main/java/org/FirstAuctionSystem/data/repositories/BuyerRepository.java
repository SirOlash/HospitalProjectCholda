package org.FirstAuctionSystem.data.repositories;

import org.FirstAuctionSystem.data.models.Buyer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends MongoRepository<Buyer, String> {

    Optional<Buyer> findByEmail(String attr0);

}

