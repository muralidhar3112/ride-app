package com.rider.ride_app.dto;

import com.rider.ride_app.entity.PaymentStatus;

public class PaymentResponse {

    private Long rideId;
    private Double amount;
    private PaymentStatus paymentStatus;

    public PaymentResponse(
            Long rideId,
            Double amount,
            PaymentStatus paymentStatus) {

        this.rideId = rideId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public Long getRideId() {
        return rideId;
    }

    public Double getAmount() {
        return amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}