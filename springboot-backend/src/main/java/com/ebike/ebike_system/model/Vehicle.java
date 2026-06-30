package com.ebike.ebike_system.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {

    @Id
    private Long id; // Assign corresponding simulator IDs

    @Column(nullable = false)
    private String location;

    @Column(name = "battery_level", nullable = false)
    private int batteryLevel;

    @Column(nullable = false)
    private String status; // "AVAILABLE", "IN_USE", "MAINTENANCE"

    public Vehicle() {}

    public Vehicle(Long id, String location, int batteryLevel, String status) {
        this.id = id;
        this.location = location;
        this.batteryLevel = batteryLevel;
        this.status = status;
    }

    // Abstract helper method for polymorphic maintenance costs
    public abstract double calculateMaintenanceCost();

    /* Getters & Setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
