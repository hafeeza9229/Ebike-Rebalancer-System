package com.ebike.ebike_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EBike")
public class EBike extends Vehicle {

    public EBike() {
        super();
    }

    public EBike(Long id, String location, int batteryLevel, String status) {
        super(id, location, batteryLevel, status);
    }

    @Override
    public double calculateMaintenanceCost() {
        // Higher base maintenance for high-tech components of EBikes if in MAINTENANCE state
        if ("MAINTENANCE".equalsIgnoreCase(getStatus())) {
            return 3800.0;
        }
        return 0.0;
    }
}
