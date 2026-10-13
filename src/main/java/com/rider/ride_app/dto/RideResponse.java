package com.rider.ride_app.dto;

import com.rider.ride_app.entity.RideStatus;

import java.time.LocalDateTime;

public class RideResponse {

    private Long id;
    private Long riderId;
    private Long driverId;

    private double pickupLatitude;
    private double pickupLongitude;

    private double destinationLatitude;
    private double destinationLongitude;

    private RideStatus status;

    private Double estimatedFare;
    private Double finalFare;

    private LocalDateTime requestedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public RideResponse(
            Long id,
            Long riderId,
            Long driverId,
            double pickupLatitude,
            double pickupLongitude,
            double destinationLatitude,
            double destinationLongitude,
            RideStatus status,
            Double estimatedFare,
            Double finalFare,
            LocalDateTime requestedAt,
            LocalDateTime startedAt,
            LocalDateTime completedAt) {

        this.id = id;
        this.riderId = riderId;
        this.driverId = driverId;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.status = status;
        this.estimatedFare = estimatedFare;
        this.finalFare = finalFare;
        this.requestedAt = requestedAt;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getRiderId() {
        return riderId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public double getPickupLatitude() {
        return pickupLatitude;
    }

    public double getPickupLongitude() {
        return pickupLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public RideStatus getStatus() {
        return status;
    }

    public Double getEstimatedFare() {
        return estimatedFare;
    }

    public Double getFinalFare() {
        return finalFare;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}