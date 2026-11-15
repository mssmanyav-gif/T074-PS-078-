package com.prepaid_recharge.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pack {

    private String packId;

    private String subscriberId;
    private String packType;

    private BigDecimal price;

    private double dailyQuotaMB;
    private double remainingDailyQuotaMB;

    private LocalDateTime startAt;
    private LocalDateTime expiryAt;

    private String status;

    public Pack() {
    }

    public Pack(String subscriberId, String packType,
                BigDecimal price, double dailyQuotaMB,
                LocalDateTime startAt, LocalDateTime expiryAt,
                String status) {

        this.subscriberId = subscriberId;
        this.packType = packType;
        this.price = price;
        this.dailyQuotaMB = dailyQuotaMB;
        this.remainingDailyQuotaMB = dailyQuotaMB;
        this.startAt = startAt;
        this.expiryAt = expiryAt;
        this.status = status;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public String getPackType() {
        return packType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public double getDailyQuotaMB() {
        return dailyQuotaMB;
    }

    public double getRemainingDailyQuotaMB() {
        return remainingDailyQuotaMB;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }

    public String getStatus() {
        return status;
    }

    public void setRemainingDailyQuotaMB(double remainingDailyQuotaMB) {
        this.remainingDailyQuotaMB = remainingDailyQuotaMB;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }
}
