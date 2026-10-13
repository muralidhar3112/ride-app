package com.rider.ride_app.service;

import com.rider.ride_app.dto.PaymentResponse;
import com.rider.ride_app.dto.RideResponse;
import com.rider.ride_app.entity.PaymentStatus;
import com.rider.ride_app.entity.Ride;
import com.rider.ride_app.entity.RideStatus;
import com.rider.ride_app.entity.User;
import com.rider.ride_app.event.PaymentCompletedEvent;
import com.rider.ride_app.event.PaymentFailedEvent;
import com.rider.ride_app.event.RideEventProducer;
import com.rider.ride_app.repository.RideRepository;
import com.rider.ride_app.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final RideEventProducer rideEventProducer;


    public PaymentService(
            RideRepository rideRepository,
            UserRepository userRepository,
            RideEventProducer rideEventProducer) {

        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.rideEventProducer = rideEventProducer;
    }

    public PaymentResponse payForRide(Long rideId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        // Only the rider can pay
        if (!ride.getRider().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Only the rider can pay for this ride");
        }

        // Ride must be completed
        if (ride.getStatus() != RideStatus.COMPLETED) {
            throw new RuntimeException(
                    "Ride must be completed before payment");
        }

        // Prevent paying twice
        if (ride.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException(
                    "Ride has already been paid");
        }

        // Simulate successful payment
        ride.setPaymentStatus(PaymentStatus.PAID);

        Ride savedRide = rideRepository.save(ride);

        System.out.println("PAYMENT SAVED: " + savedRide.getId());

        System.out.println("PUBLISHING PAYMENT EVENT: " + savedRide.getId());

        PaymentCompletedEvent event = new PaymentCompletedEvent(
                savedRide.getId(),
                savedRide.getRider().getId(),
                savedRide.getDriver() != null
                        ? savedRide.getDriver().getId()
                        : null,
                savedRide.getFinalFare()
        );

        rideEventProducer.publishPaymentCompleted(event);

        return new PaymentResponse(
                savedRide.getId(),
                savedRide.getFinalFare(),
                savedRide.getPaymentStatus()
        );
    }

    public PaymentResponse failPayment(Long rideId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        // Only the rider can make the payment attempt
        if (!ride.getRider().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Only the rider can pay for this ride");
        }

        // Ride must be completed
        if (ride.getStatus() != RideStatus.COMPLETED) {
            throw new RuntimeException(
                    "Ride must be completed before payment");
        }

        // Don't change an already paid payment
        if (ride.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException(
                    "Ride has already been paid");
        }

        ride.setPaymentStatus(PaymentStatus.FAILED);

        Ride savedRide = rideRepository.save(ride);

        PaymentFailedEvent event = new PaymentFailedEvent(
                savedRide.getId(),
                savedRide.getRider().getId(),
                savedRide.getDriver() != null
                        ? savedRide.getDriver().getId()
                        : null,
                savedRide.getFinalFare()
        );

        rideEventProducer.publishPaymentFailed(event);

        return new PaymentResponse(
                savedRide.getId(),
                savedRide.getFinalFare(),
                savedRide.getPaymentStatus()
        );
    }

    private RideResponse toResponse(Ride ride) {

        return new RideResponse(
                ride.getId(),
                ride.getRider().getId(),
                ride.getDriver() != null
                        ? ride.getDriver().getId()
                        : null,
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
}