package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.Pack;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPackRepository implements PackRepository {

    private final Map<String, Pack> packs = new ConcurrentHashMap<>();

    @Override
    public Pack save(Pack pack) {
        if (pack == null) {
            throw new IllegalArgumentException("Pack cannot be null");
        }
        if (pack.getPackId() == null || pack.getPackId().isBlank()) {
            pack.setPackId(UUID.randomUUID().toString());
        }
        packs.put(pack.getPackId(), pack);
        return pack;
    }

    @Override
    public Optional<Pack> findById(String id) {
        return Optional.ofNullable(packs.get(id));
    }

    @Override
    public List<Pack> findBySubscriberId(String subscriberId) {
        return packs.values().stream()
                .filter(pack -> subscriberId.equals(pack.getSubscriberId()))
                .toList();
    }

    @Override
    public boolean existsById(String id) {
        return packs.containsKey(id);
    }
}
