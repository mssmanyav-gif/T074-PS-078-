package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.PackPlan;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPackPlanRepository implements PackPlanRepository {

    private final Map<String, PackPlan> plans = new ConcurrentHashMap<>();

    @Override
    public PackPlan save(PackPlan packPlan) {
        if (packPlan == null) {
            throw new IllegalArgumentException("Pack plan cannot be null");
        }
        if (packPlan.getPackCode() == null || packPlan.getPackCode().isBlank()) {
            throw new IllegalArgumentException("Pack plan code is required");
        }
        plans.put(packPlan.getPackCode(), packPlan);
        return packPlan;
    }

    @Override
    public boolean existsById(String id) {
        return plans.containsKey(id);
    }

    @Override
    public Optional<PackPlan> findById(String id) {
        return Optional.ofNullable(plans.get(id));
    }

    @Override
    public Optional<PackPlan> findByPackType(String packType) {
        if (packType == null || packType.isBlank()) {
            return Optional.empty();
        }
        return plans.values().stream()
                .filter(plan -> plan.getPackType() != null && plan.getPackType().equalsIgnoreCase(packType))
                .findFirst();
    }

    @Override
    public Optional<PackPlan> findByPackCode(String packCode) {
        if (packCode == null || packCode.isBlank()) {
            return Optional.empty();
        }
        return plans.values().stream()
                .filter(plan -> plan.getPackCode() != null && plan.getPackCode().equalsIgnoreCase(packCode))
                .findFirst();
    }
}
