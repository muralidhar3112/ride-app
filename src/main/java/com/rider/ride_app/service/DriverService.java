package com.rider.ride_app.service;

import com.rider.ride_app.dto.DriverProfileRequest;
import com.rider.ride_app.dto.LocationUpdateRequest;
import com.rider.ride_app.entity.Driver;
import com.rider.ride_app.entity.Role;
import com.rider.ride_app.entity.User;
import com.rider.ride_app.repository.DriverRepository;
import com.rider.ride_app.repository.UserRepository;
import com.rider.ride_app.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import com.rider.ride_app.dto.VehicleRequest;
import com.rider.ride_app.entity.Vehicle;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public DriverService(
            DriverRepository driverRepository,
            UserRepository userRepository,
            VehicleRepository vehicleRepository) {

        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }


    public Driver createDriverProfile(
            String email,
            DriverProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (user.getRole() != Role.DRIVER) {
            throw new RuntimeException(
                    "Only drivers can create a driver profile");
        }

        if (driverRepository.existsByUser(user)) {
            throw new RuntimeException(
                    "Driver profile already exists");
        }

        if (driverRepository.existsByLicenseNumber(
                request.getLicenseNumber())) {

            throw new RuntimeException(
                    "License number already registered");
        }

        Driver driver = new Driver(
                user,
                request.getLicenseNumber()
        );

        return driverRepository.save(driver);
    }

    public Vehicle addVehicle(
            String email,
            VehicleRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        if (vehicleRepository.existsByDriver(driver)) {
            throw new RuntimeException(
                    "Driver already has a vehicle");
        }

        if (vehicleRepository.existsByRegistrationNumber(
                request.getRegistrationNumber())) {

            throw new RuntimeException(
                    "Vehicle registration number already exists");
        }

        Vehicle vehicle = new Vehicle(
                driver,
                request.getVehicleType(),
                request.getRegistrationNumber(),
                request.getModel()
        );

        return vehicleRepository.save(vehicle);
    }

    public Driver goOnline(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        if (!driver.isVerified()) {
            throw new RuntimeException(
                    "Driver is not verified");
        }

        if (!vehicleRepository.existsByDriver(driver)) {
            throw new RuntimeException(
                    "Add a vehicle before going online");
        }

        driver.setOnline(true);

        return driverRepository.save(driver);
    }

    public Driver goOffline(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        driver.setOnline(false);

        return driverRepository.save(driver);
    }

    public void verifyDriver(Long driverId) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));

        driver.setVerified(true);

        driverRepository.save(driver);
    }

    public void updateLocation(
            String email,
            LocationUpdateRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());

        driverRepository.save(driver);
    }

    public Long getDriverIdByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        return driver.getId();
    }

}