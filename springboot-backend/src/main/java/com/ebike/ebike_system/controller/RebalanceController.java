package com.ebike.ebike_system.controller;

import com.ebike.ebike_system.model.RebalanceLog;
import com.ebike.ebike_system.service.RebalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rebalance")
@CrossOrigin(origins = "*")
public class RebalanceController {

    private final RebalanceService rebalanceService;

    public RebalanceController(RebalanceService rebalanceService) {
        this.rebalanceService = rebalanceService;
    }

    @PostMapping("/trigger")
    public ResponseEntity<?> triggerRebalance() {
        String outcome = rebalanceService.triggerRebalance();
        return ResponseEntity.ok(Map.of("message", outcome));
    }

    @GetMapping("/history")
    public List<RebalanceLog> getHistory() {
        return rebalanceService.getHistory();
    }

    @GetMapping("/fuel-saved")
    public ResponseEntity<Double> getFuelSaved() {
        return ResponseEntity.ok(rebalanceService.getTotalFuelSaved());
    }
}
