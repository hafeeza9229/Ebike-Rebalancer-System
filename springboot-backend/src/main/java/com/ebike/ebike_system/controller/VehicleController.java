package com.ebike.ebike_system.controller;

import com.ebike.ebike_system.model.Vehicle;
import com.ebike.ebike_system.model.EBike;
import com.ebike.ebike_system.model.ManualBike;
import com.ebike.ebike_system.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.getAvailableVehicles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vehicleService.getVehicleById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Map<String, Object> payload) {
        String type = (String) payload.getOrDefault("type", "EBike");
        String location = (String) payload.getOrDefault("location", "Central Hub");
        int batteryLevel = ((Number) payload.getOrDefault("batteryLevel", 80)).intValue();
        String status = (String) payload.getOrDefault("status", "AVAILABLE");

        Vehicle vehicle;
        Long generatedId = 100L + vehicleService.getAllVehicles().size() + 1L;

        if ("ManualBike".equalsIgnoreCase(type)) {
            vehicle = new ManualBike(generatedId, location, batteryLevel, status);
        } else {
            vehicle = new EBike(generatedId, location, batteryLevel, status);
        }

        Vehicle saved = vehicleService.saveVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> patchStatus(
            @PathVariable Long id, 
            @RequestParam String status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer batteryLevel) {
        try {
            vehicleService.updateVehicleDetails(id, status, location, batteryLevel);
            return ResponseEntity.ok(Map.of("message", "Vehicle status and details updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.ok(Map.of("message", "Vehicle deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
