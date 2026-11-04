
package com.prepaid_recharge.repository;

import com.prepaid.engine.model.Subscriber;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscriberRepository
        extends MongoRepository<Subscriber, String> {
}

