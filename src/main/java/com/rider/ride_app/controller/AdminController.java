package com.rider.ride_app.controller;

import com.rider.ride_app.service.DriverService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DriverService driverService;

    public AdminController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PutMapping("/drivers/{driverId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public String verifyDriver(@PathVariable Long driverId) {

        driverService.verifyDriver(driverId);

        return "Driver verified successfully";
    }
}