package com.ebike.ebike_system.repository;

import com.ebike.ebike_system.model.RebalanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RebalanceLogRepository extends JpaRepository<RebalanceLog, Long> {

    @Query("SELECT COALESCE(SUM(r.fuelSaved), 0.0) FROM RebalanceLog r")
    Double getTotalFuelSaved();
}
