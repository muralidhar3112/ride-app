package com.rider.ride_app.service;

import com.rider.ride_app.dto.RideRequest;
import com.rider.ride_app.dto.RideResponse;
import com.rider.ride_app.entity.Ride;
import com.rider.ride_app.entity.RideStatus;
import com.rider.ride_app.entity.User;
import com.rider.ride_app.event.*;
import com.rider.ride_app.repository.DriverRepository;
import com.rider.ride_app.repository.RideRepository;
import com.rider.ride_app.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.rider.ride_app.entity.Driver;
import com.rider.ride_app.service.DriverLocationService;
import com.rider.ride_app.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final DriverLocationService driverLocationService;
    private final RideEventProducer rideEventProducer;
    private final NotificationService notificationService;

    public RideService(
            RideRepository rideRepository,
            UserRepository userRepository,
            DriverRepository driverRepository,
            DriverLocationService driverLocationService,
            RideEventProducer rideEventProducer,
            NotificationService notificationService) {

        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.driverLocationService = driverLocationService;
        this.rideEventProducer = rideEventProducer;
        this.notificationService = notificationService;
    }

    public RideResponse requestRide(
            String email,
            RideRequest request) {

        User rider = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        Ride ride = new Ride(
                rider,
                request.getPickupLatitude(),
                request.getPickupLongitude(),
                request.getDestinationLatitude(),
                request.getDestinationLongitude()
        );

        double distance = calculateDistance(
                request.getPickupLatitude(),
                request.getPickupLongitude(),
                request.getDestinationLatitude(),
                request.getDestinationLongitude()
        );

        double estimatedFare = 40 + (distance * 12);

        ride.setEstimatedFare(
                Math.round(estimatedFare * 100.0) / 100.0
        );

        Ride savedRide = rideRepository.save(ride);

        RideRequestedEvent event = new RideRequestedEvent(
                savedRide.getId(),
                savedRide.getRider().getId(),
                savedRide.getPickupLatitude(),
                savedRide.getPickupLongitude(),
                savedRide.getDestinationLatitude(),
                savedRide.getDestinationLongitude()
        );

        rideEventProducer.publishRideRequested(event);

        return toResponse(savedRide);
    }

    private double calculateDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double EARTH_RADIUS_KM = 6371.0;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(latDistance / 2)
                        * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2)
                        * Math.sin(lonDistance / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return EARTH_RADIUS_KM * c;
    }

    private Driver findNearestDriver(
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

            if (driver.isBusy()) {
                continue;
            }

            return driver;
        }

        return null;
    }

    public RideResponse acceptRide(Long rideId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        if (ride.getDriver() == null) {
            throw new RuntimeException(
                    "No driver has been assigned to this ride");
        }

        if (!ride.getDriver().getId().equals(driver.getId())) {
            throw new RuntimeException(
                    "This ride is not assigned to you");
        }

        if (ride.getStatus() != RideStatus.DRIVER_ASSIGNED) {
            throw new RuntimeException(
                    "Ride cannot be accepted in its current state");
        }

        ride.setStatus(RideStatus.DRIVER_ACCEPTED);

        Ride savedRide = rideRepository.save(ride);

        RideAcceptedEvent event = new RideAcceptedEvent(
                savedRide.getId(),
                savedRide.getRider().getId(),
                savedRide.getDriver().getId()
        );

        rideEventProducer.publishRideAccepted(event);

        return toResponse(savedRide);
    }

    public RideResponse startRide(Long rideId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        if (ride.getDriver() == null ||
                !ride.getDriver().getId().equals(driver.getId())) {

            throw new RuntimeException(
                    "This ride is not assigned to you");
        }

        if (ride.getStatus() != RideStatus.DRIVER_ACCEPTED) {
            throw new RuntimeException(
                    "Ride must be accepted before starting");
        }

        ride.setStatus(RideStatus.STARTED);
        ride.setStartedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);

        RideStartedEvent event = new RideStartedEvent(
                savedRide.getId(),
                savedRide.getRider().getId(),
                savedRide.getDriver().getId()
        );

        rideEventProducer.publishRideStarted(event);

        return toResponse(savedRide);
    }

    public RideResponse completeRide(Long rideId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        if (ride.getDriver() == null ||
                !ride.getDriver().getId().equals(driver.getId())) {

            throw new RuntimeException(
                    "This ride is not assigned to you");
        }

        if (ride.getStatus() != RideStatus.STARTED) {
            throw new RuntimeException(
                    "Ride must be started before completing");
        }

        double distance = calculateDistance(
                ride.getPickupLatitude(),
                ride.getPickupLongitude(),
                ride.getDestinationLatitude(),
                ride.getDestinationLongitude()
        );

        double finalFare = 40 + (distance * 12);

        ride.setFinalFare(
                Math.round(finalFare * 100.0) / 100.0
        );

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());

        driver.setBusy(false);
        driverRepository.save(driver);

        Ride savedRide = rideRepository.save(ride);

        RideCompletedEvent event = new RideCompletedEvent(
                savedRide.getId(),
                savedRide.getRider().getId(),
                savedRide.getDriver().getId(),
                savedRide.getFinalFare()
        );

        rideEventProducer.publishRideCompleted(event);

        return toResponse(savedRide);
    }

    public RideResponse cancelRide(Long rideId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        boolean isRider =
                ride.getRider().getId().equals(user.getId());

        boolean isDriver =
                ride.getDriver() != null
                        && ride.getDriver().getUser().getId()
                        .equals(user.getId());

        if (!isRider && !isDriver) {
            throw new RuntimeException(
                    "You are not part of this ride");
        }

        if (ride.getStatus() == RideStatus.STARTED ||
                ride.getStatus() == RideStatus.COMPLETED ||
                ride.getStatus() == RideStatus.CANCELLED) {

            throw new RuntimeException(
                    "Ride cannot be cancelled at this stage");
        }

        if (ride.getDriver() != null) {
            ride.getDriver().setBusy(false);
            driverRepository.save(ride.getDriver());
        }

        ride.setStatus(RideStatus.CANCELLED);

        Ride savedRide = rideRepository.save(ride);

        // Notify the other participant
        if (isRider && savedRide.getDriver() != null) {

            notificationService.createNotification(
                    savedRide.getDriver().getUser().getId(),
                    savedRide.getId(),
                    "The rider has cancelled the ride",
                    "RIDE_CANCELLED"
            );

        } else if (isDriver) {

            notificationService.createNotification(
                    savedRide.getRider().getId(),
                    savedRide.getId(),
                    "The driver has cancelled the ride",
                    "RIDE_CANCELLED"
            );
        }

        return toResponse(savedRide);
    }

    public List<Ride> getRiderRides(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return rideRepository.findByRider(user);
    }

    public List<Ride> getDriverRides(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        return rideRepository.findByDriver(driver);
    }

    private RideResponse toResponse(Ride ride) {

        Long driverId = ride.getDriver() != null
                ? ride.getDriver().getId()
                : null;

        return new RideResponse(
                ride.getId(),
                ride.getRider().getId(),
                driverId,
                ride.getPickupLatitude(),
                ride.getPickupLongitude(),
                ride.getDestinationLatitude(),
                ride.getDestinationLongitude(),
                ride.getStatus(),
                ride.getEstimatedFare(),
                ride.getFinalFare(),
                ride.getRequestedAt(),
                ride.getStartedAt(),
                ride.getCompletedAt()
        );
    }

    public void matchDriverForRide(RideRequestedEvent event) {

        Ride ride = rideRepository.findById(event.getRideId())
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        // Make sure the ride is still waiting for a driver
        if (ride.getStatus() != RideStatus.REQUESTED) {
            return;
        }

        Driver driver = findNearestDriver(
                event.getPickupLatitude(),
                event.getPickupLongitude()
        );

        if (driver == null) {
            System.out.println(
                    "No available driver found for ride: "
                            + event.getRideId()
            );
            return;
        }

        ride.setDriver(driver);
        ride.setStatus(RideStatus.DRIVER_ASSIGNED);

        driver.setBusy(true);
        driverRepository.save(driver);

        rideRepository.save(ride);

        System.out.println(
                "Driver " + driver.getId()
                        + " assigned to ride "
                        + ride.getId()
        );
    }


}