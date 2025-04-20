package org.FirstAuctionSystem.services.buyerservice;


import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.FirstAuctionSystem.data.dtorequest.BuyerRegistrationRequest;
import org.FirstAuctionSystem.data.dtorequest.BuyerResponse;
import org.FirstAuctionSystem.data.models.Buyer;
import org.FirstAuctionSystem.exceptions.BuyerCollectionException;
import org.FirstAuctionSystem.security.PasswordService;
import org.FirstAuctionSystem.services.buyerservice.IBuyer;
import org.springframework.stereotype.Service;
import org.FirstAuctionSystem.data.repositories.BuyerRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Data
public class BuyerImpl implements IBuyer {
    private final BuyerRepository buyerRepository;

    private PasswordService passwordService;

    @Override
    public void createBuyer(Buyer buyer) {
        Optional<Buyer> foundBuyer = buyerRepository.findByEmail(buyer.getEmail());
        if (foundBuyer.isPresent()){
            throw new BuyerCollectionException(BuyerCollectionException.DuplicateEmailException(buyer.getEmail()).getMessage());

        }

        buyerRepository.save(buyer);
    }
    @Override
    public long countAllBuyers() {
        return buyerRepository.count();
    }
    @Override
    public BuyerResponse createBuyerWithDTO(BuyerRegistrationRequest request)
            throws ConstraintViolationException, BuyerCollectionException {

        Optional<Buyer> foundBuyer = buyerRepository.findByEmail(request.getEmail());
        if (foundBuyer.isPresent()){
            throw new BuyerCollectionException(BuyerCollectionException.DuplicateEmailException(request.getEmail()).getMessage());

        }

        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            throw new ConstraintViolationException("username cannot be empty!", null);
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ConstraintViolationException("password cannot be empty!", null);
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ConstraintViolationException("email cannot be empty!", null);
        }
        Buyer registeredBuyer = new Buyer(request.getUserName(), request.getPassword(), request.getEmail());
        registeredBuyer.setEmail(request.getEmail());
        registeredBuyer.setUserName(request.getUserName());
        registeredBuyer.setEncryptedPassword(passwordService.hashPassword(request.getPassword()));


        Buyer buyer = buyerRepository.save(registeredBuyer);

        return new BuyerResponse(buyer);
    }



    @Override
    public Buyer buyerLogin(String email, String password) {

        Optional<Buyer> registeredBuyer = buyerRepository.findByEmail(email);
        if (registeredBuyer.isEmpty()) {
            throw new BuyerCollectionException(BuyerCollectionException.BuyerEmailNotFoundException(email).getMessage());
        }
        Buyer buyer = registeredBuyer.get();
        if (!password.equals(buyer.getEncryptedPassword())) {
            throw new BuyerCollectionException(BuyerCollectionException.InvalidEmailOrPasswordException(buyer.getEncryptedPassword()).getMessage());
        }
        return buyer;
    }


}
