package com.ebike.ebike_system.service;

import com.ebike.ebike_system.model.Station;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final StationService stationService;
    private final VehicleService vehicleService;
    private final RebalanceService rebalanceService;

    public AnalyticsService(
            StationService stationService,
            VehicleService vehicleService,
            RebalanceService rebalanceService
    ) {
        this.stationService = stationService;
        this.vehicleService = vehicleService;
        this.rebalanceService = rebalanceService;
    }

    public Map<String, Object> getSystemSummary() {

        List<Station> stations = stationService.getAllStations();
        List<Station> needRebalance = stationService.getStationsNeedingRebalance();

        long healthy = stations.stream()
                .filter(s -> !s.needsRebalancing())
                .count();

        Map<String, Object> map = new HashMap<>();
        map.put("totalStations", stations.size());
        map.put("stationsNeedingRebalance", needRebalance.size());
        map.put("healthyStations", healthy);
        map.put("totalVehicles", vehicleService.getAllVehicles().size());
        map.put("availableVehicles", vehicleService.getAvailableVehicles().size());
        map.put("totalFuelSaved", rebalanceService.getTotalFuelSaved());
        map.put("rebalanceCount", rebalanceService.getHistory().size());

        return map;
    }
}
