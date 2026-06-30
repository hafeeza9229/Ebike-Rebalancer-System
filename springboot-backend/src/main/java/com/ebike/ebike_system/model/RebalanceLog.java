package com.ebike.ebike_system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rebalance_logs")
public class RebalanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "route_taken", nullable = false)
    private String routeTaken;

    @Column(name = "stations_serviced")
    private int stationsServiced;

    @Column(name = "fuel_saved")
    private double fuelSaved;

    public RebalanceLog() {
        this.timestamp = LocalDateTime.now();
    }

    public RebalanceLog(String routeTaken, int stationsServiced, double fuelSaved) {
        this.routeTaken = routeTaken;
        this.stationsServiced = stationsServiced;
        this.fuelSaved = fuelSaved;
        this.timestamp = LocalDateTime.now();
    }

    /* Getters & Setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRouteTaken() {
        return routeTaken;
    }

    public void setRouteTaken(String routeTaken) {
        this.routeTaken = routeTaken;
    }

    public int getStationsServiced() {
        return stationsServiced;
    }

    public void setStationsServiced(int stationsServiced) {
        this.stationsServiced = stationsServiced;
    }

    public double getFuelSaved() {
        return fuelSaved;
    }

    public void setFuelSaved(double fuelSaved) {
        this.fuelSaved = fuelSaved;
    }
}
