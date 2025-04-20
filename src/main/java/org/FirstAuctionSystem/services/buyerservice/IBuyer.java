package org.FirstAuctionSystem.services.buyerservice;

import jakarta.validation.ConstraintViolationException;
import org.FirstAuctionSystem.data.dtorequest.BuyerRegistrationRequest;
import org.FirstAuctionSystem.data.dtorequest.BuyerResponse;
import org.FirstAuctionSystem.data.models.Buyer;
import org.FirstAuctionSystem.exceptions.BuyerCollectionException;

public interface IBuyer{
    public void createBuyer(Buyer buyer);

    public long countAllBuyers();
    public Buyer buyerLogin(String mail, String number);
    public BuyerResponse createBuyerWithDTO(BuyerRegistrationRequest request)
            throws ConstraintViolationException, BuyerCollectionException;

}
