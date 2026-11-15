
package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.Subscriber;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscriberRepository
        extends MongoRepository<Subscriber, String> {
}

