package com.prepaid_recharge.service;

import com.prepaid_recharge.model.Pack;
import com.prepaid_recharge.model.Subscriber;
import com.prepaid_recharge.repository.PackRepository;
import com.prepaid_recharge.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SubscriberService {

    private final SubscriberRepository repository;
    private final PackRepository packRepository;

    public SubscriberService(SubscriberRepository repository, PackRepository packRepository) {
        this.repository = repository;
        this.packRepository = packRepository;
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
        Subscriber subscriber = getSubscriber(id);
        Pack pack = packRepository.findById(packId)
                .orElseThrow(() -> new IllegalArgumentException("Pack not found"));

        if (pack.getPrice() == null) {
            throw new IllegalArgumentException("Pack price is missing");
        }

        subscriber.setCoreBalance(subscriber.getCoreBalance().add(pack.getPrice()));
        return repository.save(subscriber);
    }
}

