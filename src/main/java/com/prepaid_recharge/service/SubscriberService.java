package com.prepaid_recharge.service;

import com.prepaid_recharge.model.Subscriber;
import com.prepaid_recharge.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SubscriberService {

    private final SubscriberRepository repository;

    public SubscriberService(SubscriberRepository repository) {
        this.repository = repository;
    }

    public Subscriber registerSubscriber(
            String id, String name, String circle,
            LocalDate activationDate, BigDecimal balance) {

        if (id == null || id.isBlank()
                || name == null || name.isBlank()
                || circle == null || circle.isBlank()) {
            throw new IllegalArgumentException("Required fields cannot be empty");
        }

        if (activationDate == null || balance == null
                || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid date or balance");
        }

        if (repository.existsById(id)) {
            throw new IllegalArgumentException("Subscriber already exists");
        }

        Subscriber subscriber = new Subscriber(
                id, name, circle, activationDate, balance
        );

        return repository.save(subscriber);
    }

    public Subscriber getSubscriber(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Subscriber not found"));
    }

    public Subscriber recharge(String id, String packId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recharge'");
    }
}

