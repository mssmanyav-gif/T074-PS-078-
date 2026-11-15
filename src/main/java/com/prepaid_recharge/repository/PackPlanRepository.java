package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.PackPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PackPlanRepository extends MongoRepository<PackPlan, String> {
}