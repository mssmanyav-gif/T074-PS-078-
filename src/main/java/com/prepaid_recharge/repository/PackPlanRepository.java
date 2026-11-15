package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.PackPlan;

import java.util.Optional;

public interface PackPlanRepository {
    PackPlan save(PackPlan packPlan);
    boolean existsById(String id);
    Optional<PackPlan> findById(String id);
    Optional<PackPlan> findByPackType(String packType);
    Optional<PackPlan> findByPackCode(String packCode);
}