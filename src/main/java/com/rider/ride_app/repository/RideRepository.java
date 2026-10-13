package com.rider.ride_app.repository;

import com.rider.ride_app.entity.Driver;
import com.rider.ride_app.entity.Ride;
import com.rider.ride_app.entity.RideStatus;
import com.rider.ride_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByRider(User rider);

    List<Ride> findByStatus(RideStatus status);

    List<Ride> findByDriver(Driver driver);
}