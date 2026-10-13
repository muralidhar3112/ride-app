package com.rider.ride_app.repository;

import com.rider.ride_app.entity.Driver;
import com.rider.ride_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUser(User user);

    boolean existsByUser(User user);

    boolean existsByLicenseNumber(String licenseNumber);

    List<Driver> findByOnlineTrueAndVerifiedTrue();
}