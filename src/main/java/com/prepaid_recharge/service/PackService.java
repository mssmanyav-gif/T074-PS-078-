package com.prepaid_recharge.service;

import com.prepaid_recharge.model.Pack;
import com.prepaid_recharge.model.Subscriber;
import com.prepaid_recharge.repository.PackRepository;
import com.prepaid_recharge.repository.SubscriberRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class PackService {

    @SuppressWarnings("unused")
    private final PackRepository packRepository;
    private final SubscriberRepository subscriberRepository;

    @SuppressWarnings("unused")
    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    public PackService(PackRepository packRepository,
                       SubscriberRepository subscriberRepository) {
        this.packRepository = packRepository;
        this.subscriberRepository = subscriberRepository;
    }

    // FLOW 1: Core balance recharge
    public Subscriber rechargeBalance(String subscriberId, BigDecimal amount) {

        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subscriber not found"));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Recharge amount must be positive");
        }

        BigDecimal newBalance = subscriber.getCoreBalance().add(amount);
        subscriber.setCoreBalance(newBalance);
        return subscriber;
    }

    public Pack purchasePack(String id, String packType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'purchasePack'");
    }
}