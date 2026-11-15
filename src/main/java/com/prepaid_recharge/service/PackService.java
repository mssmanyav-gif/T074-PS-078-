package com.prepaid_recharge.service;

import com.prepaid_recharge.model.Pack;
import com.prepaid_recharge.model.PackPlan;
import com.prepaid_recharge.model.Subscriber;
import com.prepaid_recharge.repository.PackPlanRepository;
import com.prepaid_recharge.repository.PackRepository;
import com.prepaid_recharge.repository.SubscriberRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

@Service
public class PackService {

    private final PackRepository packRepository;
    private final SubscriberRepository subscriberRepository;
    private final PackPlanRepository packPlanRepository;
    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    public PackService(PackRepository packRepository,
                       SubscriberRepository subscriberRepository,
                       PackPlanRepository packPlanRepository) {
        this.packRepository = packRepository;
        this.subscriberRepository = subscriberRepository;
        this.packPlanRepository = packPlanRepository;
    }

    public Subscriber rechargeBalance(String subscriberId, BigDecimal amount) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found"));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Recharge amount must be positive");
        }

        subscriber.setCoreBalance(subscriber.getCoreBalance().add(amount));
        return subscriberRepository.save(subscriber);
    }

    public Pack purchasePack(String id, String packType) {
        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found"));

        if (packType == null || packType.isBlank()) {
            throw new IllegalArgumentException("Pack type is required");
        }

        String normalized = packType.trim().toUpperCase(Locale.ROOT);
        PackPlan plan = packPlanRepository.findByPackType(normalized)
                .or(() -> packPlanRepository.findByPackCode(normalized))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported pack type: " + packType));

        BigDecimal price = plan.getPrice();
        if (price == null) {
            throw new IllegalArgumentException("Pack price is missing");
        }

        if (subscriber.getCoreBalance().compareTo(price) < 0) {
            throw new IllegalArgumentException("Insufficient core balance");
        }

        subscriber.setCoreBalance(subscriber.getCoreBalance().subtract(price));
        subscriberRepository.save(subscriber);

        Integer validityDays = plan.getValidityDays();
        int days = 0;
        if (validityDays != null) {
            days = validityDays;
        }

        LocalDateTime start = LocalDateTime.now(IST);
        LocalDateTime expiry = calculateExpiry(start, days);

        Pack pack = new Pack(
                subscriber.getSubscriberId(),
                plan.getPackType(),
                price,
                plan.getDailyDataGb() == null ? 0.0 : plan.getDailyDataGb().doubleValue(),
                start,
                expiry,
                "ACTIVE"
        );
        pack.setRemainingDailyQuotaMB(
                plan.getDailyDataGb() == null ? 0.0 : plan.getDailyDataGb().doubleValue() * 1024.0
        );
        pack.setStartAt(start);
        pack.setExpiryAt(expiry);
        return packRepository.save(pack);
    }

    private LocalDateTime calculateExpiry(LocalDateTime start, int validityDays) {
        if (validityDays <= 0) {
            return start;
        }
        return start.plusDays(validityDays).minusSeconds(1);
    }
}
