package com.ebike.ebike_system.service;

import com.ebike.ebike_system.model.User;
import com.ebike.ebike_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final StationService stationService;
    private final VehicleService vehicleService;

    public AdminService(UserRepository userRepository,
                        StationService stationService,
                        VehicleService vehicleService) {
        this.userRepository = userRepository;
        this.stationService = stationService;
        this.vehicleService = vehicleService;
    }

    /* ---------------- USERS ---------------- */

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /* ---------------- SYSTEM CONTROL ---------------- */

    public long totalStations() {
        return stationService.getAllStations().size();
    }

    public long totalVehicles() {
        return vehicleService.getAllVehicles().size();
    }

    public long availableVehicles() {
        return vehicleService.getAvailableVehicles().size();
    }
}
