package com.rider.ride_app.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class RideRequest {

    @NotNull(message = "Pickup latitude is required")
    @DecimalMin(value = "-90.0", message = "Invalid pickup latitude")
    @DecimalMax(value = "90.0", message = "Invalid pickup latitude")
    private Double pickupLatitude;

    @NotNull(message = "Pickup longitude is required")
    @DecimalMin(value = "-180.0", message = "Invalid pickup longitude")
    @DecimalMax(value = "180.0", message = "Invalid pickup longitude")
    private Double pickupLongitude;

    @NotNull(message = "Destination latitude is required")
    @DecimalMin(value = "-90.0", message = "Invalid destination latitude")
    @DecimalMax(value = "90.0", message = "Invalid destination latitude")
    private Double destinationLatitude;

    @NotNull(message = "Destination longitude is required")
    @DecimalMin(value = "-180.0", message = "Invalid destination longitude")
    @DecimalMax(value = "180.0", message = "Invalid destination longitude")
    private Double destinationLongitude;

    public RideRequest() {
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
}