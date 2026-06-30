package com.ebike.ebike_system.service;

import com.ebike.ebike_system.model.Station;
import com.ebike.ebike_system.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final StationService stationService;
    private final VehicleService vehicleService;
    private final RebalanceService rebalanceService;

    public DashboardService(StationService stationService,
                            VehicleService vehicleService,
                            RebalanceService rebalanceService) {
        this.stationService = stationService;
        this.vehicleService = vehicleService;
        this.rebalanceService = rebalanceService;
    }

    /**
     * Main dashboard API response
     * Used by frontend dashboard cards + analytics widgets
     */
    public Map<String, Object> getDashboardSummary() {

        List<Station> stations = stationService.getAllStations();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        long totalStations = stations.size();
        long healthyStations = stations.stream()
                .filter(s -> !s.needsRebalancing())
                .count();

        long stationsNeedingRebalance = stations.stream()
                .filter(Station::needsRebalancing)
                .count();

        long totalVehicles = vehicles.size();

        long availableVehicles = vehicles.stream()
                .filter(v -> "AVAILABLE".equalsIgnoreCase(v.getStatus()))
                .count();

        long inUseVehicles = vehicles.stream()
                .filter(v -> "IN_USE".equalsIgnoreCase(v.getStatus()))
                .count();

        long maintenanceVehicles = vehicles.stream()
                .filter(v -> "MAINTENANCE".equalsIgnoreCase(v.getStatus()))
                .count();

        double totalFuelSaved = rebalanceService.getTotalFuelSaved();
        int rebalanceRuns = rebalanceService.getHistory().size();

        Map<String, Object> response = new HashMap<>();

        // Station Metrics
        response.put("totalStations", totalStations);
        response.put("healthyStations", healthyStations);
        response.put("stationsNeedingRebalance", stationsNeedingRebalance);

        // Vehicle Metrics
        response.put("totalVehicles", totalVehicles);
        response.put("availableVehicles", availableVehicles);
        response.put("inUseVehicles", inUseVehicles);
        response.put("maintenanceVehicles", maintenanceVehicles);

        // Rebalance Metrics
        response.put("rebalanceRuns", rebalanceRuns);
        response.put("fuelSaved", totalFuelSaved);

        // System Health
        response.put("systemStatus", stationsNeedingRebalance > 0 ? "ATTENTION" : "HEALTHY");
        response.put("timestamp", System.currentTimeMillis());

        // Derived Insights (useful for AI/chat/dashboard cards)
        response.put("utilizationRate",
                totalVehicles == 0 ? 0.0 :
                        (double) inUseVehicles / totalVehicles * 100.0
        );

        response.put("stationHealthRate",
                totalStations == 0 ? 0.0 :
                        (double) healthyStations / totalStations * 100.0
        );

        response.put("rebalanceNeededRate",
                totalStations == 0 ? 0.0 :
                        (double) stationsNeedingRebalance / totalStations * 100.0
        );

        return response;
    }

    /**
     * Lightweight stats for topbar widgets
     */
    public Map<String, Object> getQuickStats() {

        List<Station> stations = stationService.getAllStations();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStations", stations.size());
        stats.put("needsRebalance", stationService.getStationsNeedingRebalance().size());
        stats.put("timestamp", System.currentTimeMillis());

        return stats;
    }
}
