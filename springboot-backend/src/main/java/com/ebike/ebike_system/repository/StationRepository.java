package com.ebike.ebike_system.repository;

import com.ebike.ebike_system.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findByCurrentBikesLessThan(int count);
}
