package com.prepaid_recharge;

import com.prepaid_recharge.model.Pack;
import com.prepaid_recharge.model.Subscriber;
import com.prepaid_recharge.service.PackService;
import com.prepaid_recharge.service.SubscriberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrepaidRechargeApplicationTests {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private PackService packService;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldRegisterSubscriberAndRechargeBalance() {
        String id = "SUB-" + UUID.randomUUID();
        Subscriber subscriber = subscriberService.registerSubscriber(
                id,
                "Test User",
                "Karnataka",
                LocalDate.now(),
                new BigDecimal("100.00")
        );

        assertEquals(id, subscriber.getSubscriberId());
        assertEquals(new BigDecimal("100.00"), subscriber.getCoreBalance());
        assertEquals("ACTIVE", subscriber.getStatus());

        Subscriber recharged = packService.rechargeBalance(id, new BigDecimal("25.00"));
        assertEquals(new BigDecimal("125.00"), recharged.getCoreBalance());
    }

    @Test
    void shouldPurchaseUnlimitedPackWhenBalanceIsSufficient() {
        String id = "PACK-" + UUID.randomUUID();
        subscriberService.registerSubscriber(
                id,
                "Pack User",
                "Maharashtra",
                LocalDate.now(),
                new BigDecimal("500.00")
        );

        Pack pack = packService.purchasePack(id, "UNLIMITED");

        assertNotNull(pack.getPackId());
        assertEquals("UNLIMITED", pack.getPackType());
        assertTrue(pack.getPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void shouldRejectDuplicateSubscriber() {
        String id = "DUP-" + UUID.randomUUID();
        subscriberService.registerSubscriber(
                id,
                "Original User",
                "Delhi",
                LocalDate.now(),
                new BigDecimal("50.00")
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> subscriberService.registerSubscriber(
                        id,
                        "Duplicate User",
                        "Delhi",
                        LocalDate.now(),
                        new BigDecimal("50.00")
                )
        );

        assertTrue(exception.getMessage().contains("already exists"));
    }
}
