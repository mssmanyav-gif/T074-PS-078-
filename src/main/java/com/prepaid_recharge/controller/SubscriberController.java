package com.prepaid_recharge.controller;

import com.prepaid_recharge.model.Subscriber;
import com.prepaid_recharge.service.SubscriberService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {

    private final SubscriberService service;

    public SubscriberController(SubscriberService service) {
        this.service = service;
    }

    // Register a new subscriber
    @PostMapping
    public Subscriber register(@RequestBody RegistrationRequest request) {
        return service.registerSubscriber(
                request.subscriberId(),
                request.name(),
                request.homeCircle(),
                request.activationDate(),
                request.initialBalance()
        );
    }

    // Get subscriber details
    @GetMapping("/{id}")
    public Subscriber getSubscriber(@PathVariable String id) {
        return service.getSubscriber(id);
    }

    // Recharge by pack 
    @PostMapping("/{id}/recharge-pack")
    public Subscriber rechargeWithPack(@PathVariable String id,
                                       @RequestBody PackRechargeRequest request) {
        return service.recharge(id, request.packId());
    }

    public record RegistrationRequest(
            String subscriberId,
            String name,
            String homeCircle,
            LocalDate activationDate,
            BigDecimal initialBalance
    ) {}

    public record PackRechargeRequest(String packId) {}
}


