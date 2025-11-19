package com.example.trackingservice.controller;

import com.example.trackingservice.dto.TrackingDto;
import com.example.trackingservice.entity.TrackingStatus;
import com.example.trackingservice.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracking")
@CrossOrigin(origins = "*")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<TrackingDto> getTrackingInfo(@PathVariable String trackingNumber) {
        TrackingDto tracking = trackingService.getTrackingInfo(trackingNumber);
        return ResponseEntity.ok(tracking);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<TrackingDto> getTrackingByOrderId(@PathVariable Long orderId) {
        TrackingDto tracking = trackingService.getTrackingByOrderId(orderId);
        return ResponseEntity.ok(tracking);
    }

    @PostMapping
    public ResponseEntity<TrackingDto> createTracking(@RequestBody TrackingDto trackingDto) {
        TrackingDto created = trackingService.createTracking(trackingDto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{trackingNumber}/status")
    public ResponseEntity<TrackingDto> updateTrackingStatus(
            @PathVariable String trackingNumber,
            @RequestParam TrackingStatus status,
            @RequestParam(required = false) String location) {
        TrackingDto updated = trackingService.updateTrackingStatus(trackingNumber, status, location);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TrackingDto>> getTrackingByStatus(@PathVariable TrackingStatus status) {
        List<TrackingDto> trackings = trackingService.getTrackingByStatus(status);
        return ResponseEntity.ok(trackings);
    }
}