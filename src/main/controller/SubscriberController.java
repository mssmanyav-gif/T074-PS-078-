package com.prepaid_recharge.controller;

import com.prepaid.engine.model.Subscriber;
import com.prepaid.engine.service.SubscriberService;
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

    @GetMapping("/{id}")
    public Subscriber getSubscriber(@PathVariable String id) {
        return service.getSubscriber(id);
    }

    public record RegistrationRequest(
            String subscriberId,
            String name,
            String homeCircle,
            LocalDate activationDate,
            BigDecimal initialBalance
    ) {}
}

