package com.ebike.ebike_system.service;

import com.ebike.ebike_system.model.RebalanceLog;
import com.ebike.ebike_system.model.Station;
import com.ebike.ebike_system.repository.RebalanceLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RebalanceService {

    private final StationService stationService;
    private final RebalanceLogRepository rebalanceLogRepository;

    public RebalanceService(StationService stationService,
                            RebalanceLogRepository rebalanceLogRepository) {
        this.stationService = stationService;
        this.rebalanceLogRepository = rebalanceLogRepository;
    }

    public String triggerRebalance() {

        List<Station> stations = stationService.getStationsNeedingRebalance();

        if (stations.isEmpty()) {
            return "All stations balanced. No action needed.";
        }

        String route = buildRoute(stations);

        RebalanceLog log = new RebalanceLog();
        log.setRouteTaken(route);
        log.setStationsServiced(stations.size());
        log.setFuelSaved(stations.size() * 1.5);

        rebalanceLogRepository.save(log);

        return "Rebalance triggered: " + route;
    }

    public List<RebalanceLog> getHistory() {
        return rebalanceLogRepository.findAll();
    }

    public Double getTotalFuelSaved() {
        return rebalanceLogRepository.getTotalFuelSaved();
    }

    private String buildRoute(List<Station> stations) {
        return stations.stream()
                .map(Station::getName)
                .reduce((a, b) -> a + " \u2192 " + b) // u2192 is Arrow right (→)
                .orElse("");
    }
}
