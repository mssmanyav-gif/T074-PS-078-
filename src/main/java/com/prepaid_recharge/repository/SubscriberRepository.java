
package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.Subscriber;

import java.util.Optional;

public interface SubscriberRepository {
    Subscriber save(Subscriber subscriber);
    Optional<Subscriber> findById(String id);
    boolean existsById(String id);
}

