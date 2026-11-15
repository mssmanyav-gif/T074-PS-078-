package com.prepaid_recharge.controller;

import com.prepaid_recharge.model.Pack;
import com.prepaid_recharge.model.Subscriber;
import com.prepaid_recharge.service.PackService;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class PackController {

    private final PackService packService;

    public PackController(PackService packService) {
        this.packService = packService;
    }

    // Core balance recharge: POST /api/subscribers/{id}/recharge
    @PostMapping("/subscribers/{id}/recharge")
    public Subscriber recharge(
            @PathVariable String id,
            @RequestBody RechargeRequest request) {

        return packService.rechargeBalance(id, request.amount());
    }

    // Purchase unlimited pack: POST /api/subscribers/{id}/packs
    @PostMapping("/subscribers/{id}/packs")
    public Pack purchasePack(
            @PathVariable String id,
            @RequestBody PackRequest request) {

        return packService.purchasePack(id, request.packType());
    }

    public record RechargeRequest(BigDecimal amount) {}

    public record PackRequest(String packType) {}
}

