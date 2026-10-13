package com.rider.ride_app.controller;

import com.rider.ride_app.dto.DriverProfileRequest;
import com.rider.ride_app.dto.LocationUpdateRequest;
import com.rider.ride_app.entity.Driver;
import com.rider.ride_app.entity.Vehicle;
import com.rider.ride_app.service.DriverLocationService;
import com.rider.ride_app.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.rider.ride_app.dto.VehicleRequest;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;
    private final DriverLocationService driverLocationService;

    public DriverController(
            DriverService driverService,
            DriverLocationService driverLocationService) {

        this.driverService = driverService;
        this.driverLocationService = driverLocationService;
    }
    @PostMapping("/profile")
    @PreAuthorize("hasRole('DRIVER')")
    public Driver createProfile(
            Authentication authentication,
            @Valid @RequestBody DriverProfileRequest request) {

        String email = authentication.getName();

        return driverService.createDriverProfile(
                email,
                request
        );
    }

    @PostMapping("/vehicle")
    @PreAuthorize("hasRole('DRIVER')")
    public Vehicle addVehicle(
            Authentication authentication,
            @Valid @RequestBody VehicleRequest request) {

        String email = authentication.getName();

        return driverService.addVehicle(email, request);
    }


    @PutMapping("/location")
    @PreAuthorize("hasRole('DRIVER')")
    public String updateLocation(
            Authentication authentication,
            @Valid @RequestBody LocationUpdateRequest request) {

        String email = authentication.getName();

        driverService.updateLocation(
                email,
                request
        );

        Long driverId = driverService.getDriverIdByEmail(email);

        driverLocationService.updateLocation(
                driverId,
                request.getLatitude(),
                request.getLongitude()
        );

        return "Location updated successfully";
    }

    @GetMapping("/nearby")
    @PreAuthorize("hasRole('DRIVER')")
    public List<String> findNearbyDrivers(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        return driverLocationService.findNearbyDrivers(
                latitude,
                longitude,
                5
        );
    }

    @PostMapping("/online")
    @PreAuthorize("hasRole('DRIVER')")
    public String goOnline(Authentication authentication) {

        String email = authentication.getName();

        Driver driver = driverService.goOnline(email);

        driverLocationService.setDriverOnline(driver.getId());

        return "Driver is now online";
    }

    @PostMapping("/offline")
    @PreAuthorize("hasRole('DRIVER')")
    public String goOffline(Authentication authentication) {

        String email = authentication.getName();

        Driver driver = driverService.goOffline(email);

        driverLocationService.setDriverOffline(driver.getId());

        return "Driver is now offline";
    }
}