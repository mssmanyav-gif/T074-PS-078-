package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.Pack;

import java.util.List;
import java.util.Optional;

public interface PackRepository {
    Pack save(Pack pack);
    Optional<Pack> findById(String id);
    List<Pack> findBySubscriberId(String subscriberId);
    boolean existsById(String id);
}
