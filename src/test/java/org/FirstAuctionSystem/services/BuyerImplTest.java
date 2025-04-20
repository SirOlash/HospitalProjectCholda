package org.FirstAuctionSystem.services;

import org.FirstAuctionSystem.data.models.Buyer;
import org.FirstAuctionSystem.exceptions.BuyerCollectionException;
import org.FirstAuctionSystem.services.buyerservice.BuyerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.FirstAuctionSystem.data.repositories.BuyerRepository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class BuyerImplTest {

    @Autowired
    private BuyerImpl buyerService;

    @Autowired
    private BuyerRepository buyerRepository;

    @BeforeEach
    public void setUp() {
        buyerRepository.deleteAll();

        Buyer buyer = new Buyer("john", "john@gmail.com", "john123");
        buyer.setUserName("user");
        buyer.setEmail("user@gmail.com");
        buyer.setEncryptedPassword("1234");

        buyerService.createBuyer(buyer);

    }
    @Test
    public void testOneCreateBuyerAndBuyerCountIsOne() {


        assertEquals(1, buyerService.countAllBuyers());

    }
    @Test
    public void testDuplicateEmailExceptionThrown_ForDuplicateEmail() {
        Buyer buyer2 = new Buyer("John", "john@gmail.com", "john123");
        buyer2.setUserName("Victor");
        buyer2.setEmail("user@gmail.com");
        buyer2.setEncryptedPassword("0909");

        assertThrows(BuyerCollectionException.class, () ->  buyerService.createBuyer(buyer2));

    }
    @Test
    public void testBuyerCanLogin_Successfully() {
        Buyer loggedInBuyer = buyerService.buyerLogin("user@gmail.com", "1234");
        assertNotNull(loggedInBuyer);
        assertEquals("user", loggedInBuyer.getUserName());
        assertEquals("user@gmail.com", loggedInBuyer.getEmail());

    }
    @Test
    public void testThrowsWrongEmailOrPasswordException_ForWrongPassword() {
        assertThrows(BuyerCollectionException.class, () ->  buyerService.buyerLogin("user@gmail.com", "wrong123"));
    }
    @Test
    public void testBuyerEmailNotFoundException_ForNonExistentEmail() {
        assertThrows(BuyerCollectionException.class, () ->  buyerService.buyerLogin("john@gmail.com", "1234"));
    }
}
