package com.ebike.ebike_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ManualBike")
public class ManualBike extends Vehicle {

    public ManualBike() {
        super();
    }

    public ManualBike(Long id, String location, int batteryLevel, String status) {
        super(id, location, batteryLevel, status);
    }

    @Override
    public double calculateMaintenanceCost() {
        // Manual standard structural maintenance is low and efficient
        if ("MAINTENANCE".equalsIgnoreCase(getStatus())) {
            return 1200.0;
        }
        return 0.0;
    }
}
