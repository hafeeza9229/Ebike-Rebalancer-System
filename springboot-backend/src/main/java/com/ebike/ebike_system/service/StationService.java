package com.ebike.ebike_system.service;

import com.ebike.ebike_system.model.Station;
import com.ebike.ebike_system.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station getStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found: " + id));
    }

    public Station saveStation(Station station) {
        return stationRepository.save(station);
    }

    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }

    public Station updateStation(Long id, Station updated) {
        Station existing = getStationById(id);
        existing.setName(updated.getName());
        existing.setLocation(updated.getLocation());
        existing.setMaxCapacity(updated.getMaxCapacity());
        if (updated.getCurrentBikes() >= 0 && updated.getCurrentBikes() <= updated.getMaxCapacity()) {
            existing.setCurrentBikes(updated.getCurrentBikes());
        }
        return stationRepository.save(existing);
    }

    public String rentBike(Long id) {
        Station s = getStationById(id);

        if (s.rentBike()) {
            stationRepository.save(s);
            return "Bike rented from " + s.getName();
        }
        return "No bikes available";
    }

    public String returnBike(Long id) {
        Station s = getStationById(id);

        if (s.returnBike()) {
            stationRepository.save(s);
            return "Bike returned to " + s.getName();
        }
        return "Station full";
    }

    public List<Station> getStationsNeedingRebalance() {
        // Less than 3 bikes represents a depleted station in need of replenishment
        return stationRepository.findByCurrentBikesLessThan(3);
    }
}
