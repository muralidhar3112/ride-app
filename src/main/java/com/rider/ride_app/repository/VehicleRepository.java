package com.rider.ride_app.repository;

import com.rider.ride_app.entity.Driver;
import com.rider.ride_app.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByDriver(Driver driver);

    boolean existsByDriver(Driver driver);

    boolean existsByRegistrationNumber(String registrationNumber);
}