package com.rider.ride_app.controller;

import com.rider.ride_app.dto.PaymentResponse;
import com.rider.ride_app.dto.RideResponse;
import com.rider.ride_app.service.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PutMapping("/ride/{rideId}")
    @PreAuthorize("hasRole('RIDER')")
    public PaymentResponse payForRide(
            @PathVariable Long rideId,
            Authentication authentication) {

        return paymentService.payForRide(
                rideId,
                authentication.getName()
        );
    }

    @PutMapping("/ride/{rideId}/fail")
    @PreAuthorize("hasRole('RIDER')")
    public PaymentResponse failPayment(
            @PathVariable Long rideId,
            Authentication authentication) {

        return paymentService.failPayment(
                rideId,
                authentication.getName()
        );
    }
}