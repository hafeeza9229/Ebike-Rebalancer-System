package com.ebike.ebike_system;

import com.ebike.ebike_system.model.Station;
import com.ebike.ebike_system.model.User;
import com.ebike.ebike_system.model.EBike;
import com.ebike.ebike_system.model.ManualBike;
import com.ebike.ebike_system.repository.StationRepository;
import com.ebike.ebike_system.repository.UserRepository;
import com.ebike.ebike_system.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EbikeSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbikeSystemApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initDatabase(StationRepository stationRepository,
                                  VehicleRepository vehicleRepository,
                                  UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed Stations
            if (stationRepository.count() == 0) {
                stationRepository.save(new Station(1L, "Central Hub", "Downtown Boulevard", 12, 15));
                stationRepository.save(new Station(2L, "Saddar Metro", "Saddar Main Crossing", 2, 20));
                stationRepository.save(new Station(3L, "Defense Chowk", "DHA Phase 2 Crossing", 8, 14));
                stationRepository.save(new Station(4L, "Clifton Beach", "Clifton Marina Road", 1, 12));
                stationRepository.save(new Station(5L, "Gulshan Ground", "Gulshan Block 4 Sector", 15, 15));
            }

            // Seed Vehicles
            if (vehicleRepository.count() == 0) {
                EBike v1 = new EBike(101L, "Saddar Metro", 85, "AVAILABLE");
                EBike v2 = new EBike(102L, "Clifton Beach", 14, "AVAILABLE");
                ManualBike v3 = new ManualBike(103L, "Central Hub", 100, "AVAILABLE");
                EBike v4 = new EBike(104L, "Defense Chowk", 55, "IN_USE");
                EBike v5 = new EBike(105L, "Gulshan Ground", 92, "MAINTENANCE");
                
                vehicleRepository.save(v1);
                vehicleRepository.save(v2);
                vehicleRepository.save(v3);
                vehicleRepository.save(v4);
                vehicleRepository.save(v5);
            }

            // Seed Users
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin@ebike.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepository.save(admin);

                User user = new User();
                user.setUsername("user@ebike.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("USER");
                userRepository.save(user);
            }
        };
    }
}
