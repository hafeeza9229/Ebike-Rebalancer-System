package com.ebike.ebike_system.controller;

import com.ebike.ebike_system.model.Station;
import com.ebike.ebike_system.service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin(origins = "*")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(stationService.getStationById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        if (station.getId() == null) {
            station.setId(System.currentTimeMillis() % 100000L); // virtual manual unique id
        }
        Station saved = stationService.saveStation(station);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStation(@PathVariable Long id, @RequestBody Station station) {
        try {
            Station updated = stationService.updateStation(id, station);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStation(@PathVariable Long id) {
        try {
            stationService.deleteStation(id);
            return ResponseEntity.ok(Map.of("message", "Station deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/rent")
    public ResponseEntity<?> rentBike(@PathVariable Long id) {
        String result = stationService.rentBike(id);
        if (result.contains("rented")) {
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", result));
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnBike(@PathVariable Long id) {
        String result = stationService.returnBike(id);
        if (result.contains("returned")) {
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", result));
        }
    }
}
