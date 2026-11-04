
package com.prepaid_recharge.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "subscribers")
public class Subscriber {

    @Id
    private String subscriberId;

    private String name;
    private String homeCircle;
    private LocalDate activationDate;
    private BigDecimal coreBalance;
    private String status;

    public Subscriber() {
    }

    public Subscriber(String subscriberId, String name,
                      String homeCircle, LocalDate activationDate,
                      BigDecimal coreBalance) {
        this.subscriberId = subscriberId;
        this.name = name;
        this.homeCircle = homeCircle;
        this.activationDate = activationDate;
        this.coreBalance = coreBalance;
        this.status = "ACTIVE";
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public String getName() {
        return name;
    }

    public String getHomeCircle() {
        return homeCircle;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public BigDecimal getCoreBalance() {
        return coreBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setCoreBalance(BigDecimal coreBalance) {
        this.coreBalance = coreBalance;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
