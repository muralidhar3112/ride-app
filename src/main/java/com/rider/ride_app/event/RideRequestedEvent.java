package com.rider.ride_app.event;

public class RideRequestedEvent {

    private Long rideId;
    private Long riderId;
    private double pickupLatitude;
    private double pickupLongitude;
    private double destinationLatitude;
    private double destinationLongitude;

    public RideRequestedEvent() {
    }

    public RideRequestedEvent(
            Long rideId,
            Long riderId,
            double pickupLatitude,
            double pickupLongitude,
            double destinationLatitude,
            double destinationLongitude) {

        this.rideId = rideId;
        this.riderId = riderId;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    public Long getRideId() {
        return rideId;
    }

    public Long getRiderId() {
        return riderId;
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
}