package com.rider.ride_app.event;

public class RideAcceptedEvent {

    private Long rideId;
    private Long riderId;
    private Long driverId;

    public RideAcceptedEvent() {
    }

    public RideAcceptedEvent(
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