package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.Subscriber;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySubscriberRepository implements SubscriberRepository {

    private final Map<String, Subscriber> subscribers = new ConcurrentHashMap<>();

    @Override
    public Subscriber save(Subscriber subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("Subscriber cannot be null");
        }
        subscribers.put(subscriber.getSubscriberId(), subscriber);
        return subscriber;
    }

    @Override
    public Optional<Subscriber> findById(String id) {
        return Optional.ofNullable(subscribers.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return subscribers.containsKey(id);
    }
}
