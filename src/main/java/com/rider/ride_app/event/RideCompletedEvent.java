package com.rider.ride_app.event;

public class RideCompletedEvent {

    private Long rideId;
    private Long riderId;
    private Long driverId;
    private double finalFare;

    public RideCompletedEvent() {
    }

    public RideCompletedEvent(
            Long rideId,
            Long riderId,
            Long driverId,
            double finalFare) {

        this.rideId = rideId;
        this.riderId = riderId;
        this.driverId = driverId;
        this.finalFare = finalFare;
    }

    public Long getRideId() {
        return rideId;
    }

    public Long getRiderId() {
        return riderId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public double getFinalFare() {
        return finalFare;
    }
}