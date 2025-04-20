package org.FirstAuctionSystem.data.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.FirstAuctionSystem.data.models.Item;


@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
}
