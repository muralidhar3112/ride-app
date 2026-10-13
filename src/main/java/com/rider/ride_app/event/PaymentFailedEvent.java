package com.rider.ride_app.event;

public class PaymentFailedEvent {

    private Long rideId;
    private Long riderId;
    private Long driverId;
    private double amount;

    public PaymentFailedEvent() {
    }

    public PaymentFailedEvent(
            Long rideId,
            Long riderId,
            Long driverId,
            double amount) {

        this.rideId = rideId;
        this.riderId = riderId;
        this.driverId = driverId;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }
}