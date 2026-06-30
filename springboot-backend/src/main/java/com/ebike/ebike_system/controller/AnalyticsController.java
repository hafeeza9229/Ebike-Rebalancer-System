package com.ebike.ebike_system.controller;

import com.ebike.ebike_system.service.AnalyticsService;
import com.ebike.ebike_system.service.DashboardService;
import com.ebike.ebike_system.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final DashboardService dashboardService;
    private final FileService fileService;

    public AnalyticsController(AnalyticsService analyticsService,
                               DashboardService dashboardService,
                               FileService fileService) {
        this.analyticsService = analyticsService;
        this.dashboardService = dashboardService;
        this.fileService = fileService;
    }

    @GetMapping("/analytics/summary")
    public Map<String, Object> getSystemSummary() {
        // Combined dashboard view summary requested by frontend
        return dashboardService.getDashboardSummary();
    }

    @GetMapping("/export/stations/csv")
    public ResponseEntity<byte[]> exportStationsToCsv() {
        try {
            fileService.exportStationsToCSV();
            java.io.File csvFile = new java.io.File("data/stations.csv");
            if (!csvFile.exists()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            byte[] fileBytes = java.nio.file.Files.readAllBytes(csvFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "stations_export.csv");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
