package com.rider.ride_app.controller;

import com.rider.ride_app.dto.RideRequest;
import com.rider.ride_app.dto.RideResponse;
import com.rider.ride_app.entity.Ride;
import com.rider.ride_app.service.RideService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    @PreAuthorize("hasRole('RIDER')")
    public RideResponse requestRide(
            Authentication authentication,
            @Valid @RequestBody RideRequest request) {

        String email = authentication.getName();

        return rideService.requestRide(
                email,
                request
        );
    }

    @PutMapping("/{rideId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse acceptRide(
            @PathVariable Long rideId,
            Authentication authentication) {

        return rideService.acceptRide(
                rideId,
                authentication.getName()
        );
    }

    @PostMapping("/{rideId}/start")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse startRide(
            @PathVariable Long rideId,
            Authentication authentication) {

        String email = authentication.getName();

        return rideService.startRide(rideId, email);
    }

    @PostMapping("/{rideId}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse completeRide(
            @PathVariable Long rideId,
            Authentication authentication) {

        String email = authentication.getName();

        return rideService.completeRide(rideId, email);
    }

    @PutMapping("/{rideId}/cancel")
    @PreAuthorize("hasAnyRole('RIDER', 'DRIVER')")
    public RideResponse cancelRide(
            @PathVariable Long rideId,
            Authentication authentication) {

        return rideService.cancelRide(
                rideId,
                authentication.getName()
        );
    }

    @GetMapping("/my-rides")
    @PreAuthorize("hasRole('RIDER')")
    public List<Ride> getMyRides(
            Authentication authentication) {

        return rideService.getRiderRides(
                authentication.getName()
        );
    }

    @GetMapping("/driver-rides")
    @PreAuthorize("hasRole('DRIVER')")
    public List<Ride> getDriverRides(
            Authentication authentication) {

        return rideService.getDriverRides(
                authentication.getName()
        );
    }

}