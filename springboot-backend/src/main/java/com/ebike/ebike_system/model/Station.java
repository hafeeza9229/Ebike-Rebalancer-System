package com.ebike.ebike_system.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stations")
public class Station {

    @Id
    private Long id; // We can assign manual IDs or auto

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(name = "current_bikes", nullable = false)
    private int currentBikes;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    public Station() {}

    public Station(Long id, String name, String location, int currentBikes, int maxCapacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.currentBikes = currentBikes;
        this.maxCapacity = maxCapacity;
    }

    /* Helper metrics business methods */

    public boolean needsRebalancing() {
        if (maxCapacity == 0) return false;
        return ((double) currentBikes / maxCapacity) < 0.2;
    }

    public boolean rentBike() {
        if (currentBikes > 0) {
            currentBikes--;
            return true;
        }
        return false;
    }

    public boolean returnBike() {
        if (currentBikes < maxCapacity) {
            currentBikes++;
            return true;
        }
        return false;
    }

    /* Getters & Setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCurrentBikes() {
        return currentBikes;
    }

    public void setCurrentBikes(int currentBikes) {
        this.currentBikes = currentBikes;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
