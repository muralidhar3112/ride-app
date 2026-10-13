package com.rider.ride_app.service;

import com.rider.ride_app.entity.Driver;
import com.rider.ride_app.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideMatchingService {

    private final DriverRepository driverRepository;
    private final DriverLocationService driverLocationService;

    public RideMatchingService(
            DriverRepository driverRepository,
            DriverLocationService driverLocationService) {

        this.driverRepository = driverRepository;
        this.driverLocationService = driverLocationService;
    }

    public Driver findNearestDriver(
            double pickupLatitude,
            double pickupLongitude) {

        List<String> nearbyDriverIds =
                driverLocationService.findNearbyDrivers(
                        pickupLatitude,
                        pickupLongitude,
                        5
                );

        for (String driverId : nearbyDriverIds) {

            Driver driver = driverRepository.findById(
                    Long.valueOf(driverId)
            ).orElse(null);

            if (driver == null) {
                continue;
            }

            if (!driver.isVerified()) {
                continue;
            }

            if (!driver.isOnline()) {
                continue;
            }

            return driver;
        }

        return null;
    }
}