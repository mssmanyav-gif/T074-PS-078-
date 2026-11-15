package com.prepaid_recharge.repository;

import com.prepaid_recharge.model.Pack;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PackRepository extends MongoRepository<Pack, String> {

    List<Pack> findBySubscriberId(String subscriberId);
}
