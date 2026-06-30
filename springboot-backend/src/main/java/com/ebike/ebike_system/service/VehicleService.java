package com.ebike.ebike_system.service;

import com.ebike.ebike_system.model.Vehicle;
import com.ebike.ebike_system.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus("AVAILABLE");
    }

    public void updateStatus(Long id, String status) {
        updateVehicleDetails(id, status, null, null);
    }

    public void updateVehicleDetails(Long id, String status, String location, Integer batteryLevel) {
        Vehicle v = getVehicleById(id);
        if (status != null && !status.trim().isEmpty()) {
            v.setStatus(status);
        }
        if (location != null && !location.trim().isEmpty()) {
            v.setLocation(location);
        }
        if (batteryLevel != null) {
            v.setBatteryLevel(batteryLevel);
        }
        vehicleRepository.save(v);
    }

    public double getMaintenanceCost(Long id) {
        return getVehicleById(id).calculateMaintenanceCost();
    }

    public List<String> getMaintenanceSummary() {
        return vehicleRepository.findAll().stream()
                .map(v -> v.getClass().getSimpleName()
                        + " #" + v.getId()
                        + " | Location: " + v.getLocation()
                        + " | Status: " + v.getStatus()
                        + " | Cost: Rs." + v.calculateMaintenanceCost())
                .toList();
    }
}
