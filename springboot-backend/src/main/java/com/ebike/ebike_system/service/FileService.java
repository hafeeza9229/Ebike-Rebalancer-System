package com.ebike.ebike_system.service;

import com.ebike.ebike_system.model.Station;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final StationService stationService;

    public FileService(StationService stationService) {
        this.stationService = stationService;
    }

    public String exportStationsToCSV() {

        String path = "data/stations.csv";

        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();

            List<Station> stations = stationService.getAllStations();

            try (FileWriter writer = new FileWriter(path)) {
                writer.write("ID,Name,Location,Bikes,Capacity\n");

                for (Station s : stations) {
                    writer.write(
                            s.getId() + "," +
                                    s.getName() + "," +
                                    s.getLocation() + "," +
                                    s.getCurrentBikes() + "," +
                                    s.getMaxCapacity() + "\n"
                    );
                }
            }

            return "Exported " + stations.size() + " stations";

        } catch (IOException e) {
            return "Export failed: " + e.getMessage();
        }
    }
}
