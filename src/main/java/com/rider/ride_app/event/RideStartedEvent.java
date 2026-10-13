package com.rider.ride_app.event;

public class RideStartedEvent {

    private Long rideId;
    private Long riderId;
    private Long driverId;

    public RideStartedEvent() {
    }

    public RideStartedEvent(
            Long rideId,
            Long riderId,
            Long driverId) {

        this.rideId = rideId;
        this.riderId = riderId;
        this.driverId = driverId;
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
}