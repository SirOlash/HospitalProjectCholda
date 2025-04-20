package org.FirstAuctionSystem.data.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.FirstAuctionSystem.data.models.Payment;


@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

}
