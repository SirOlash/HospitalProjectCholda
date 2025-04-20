package org.FirstAuctionSystem.controllers;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.FirstAuctionSystem.data.dtorequest.BuyerRegistrationRequest;
import org.FirstAuctionSystem.data.dtorequest.BuyerResponse;
import org.FirstAuctionSystem.data.repositories.BuyerRepository;
import org.FirstAuctionSystem.exceptions.BuyerCollectionException;
import org.FirstAuctionSystem.services.buyerservice.BuyerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/buyers")
public class BuyerController {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private BuyerImpl buyerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerBuyer(
            @Valid @RequestBody BuyerRegistrationRequest registrationRequest) {
        try {
            BuyerResponse createdBuyer = buyerService.createBuyerWithDTO(registrationRequest);
            return new ResponseEntity<> (createdBuyer, HttpStatus.OK);
        }
        catch(ConstraintViolationException e){
////
            return new ResponseEntity<>(errorResponse,  HttpStatus.BAD_REQUEST);
        }
        catch(BuyerCollectionException e){
//            ErrorResponse errorResponse = ErrorResponse.builder(e, HttpStatus.CONFLICT, e.getMessage())
//                    .build();
//            return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), LocalDateTime.now(), HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
        }
    }


}
