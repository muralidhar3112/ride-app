package com.rider.ride_app.event;

import com.rider.ride_app.service.NotificationService;
import com.rider.ride_app.service.RideService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.rider.ride_app.event.RideAcceptedEvent;
import com.rider.ride_app.event.RideStartedEvent;
import com.rider.ride_app.event.RideCompletedEvent;

@Service
public class RideEventConsumer {

    private final RideService rideService;
    private final NotificationService notificationService;

    public RideEventConsumer(
            RideService rideService,
            NotificationService notificationService) {

        this.rideService = rideService;
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "ride-requested",
            groupId = "ride-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeRideRequested(
            RideRequestedEvent event) {

        System.out.println("RideRequested event received:");
        System.out.println("Ride ID: " + event.getRideId());
        System.out.println("Rider ID: " + event.getRiderId());

        System.out.println(
                "Pickup: "
                        + event.getPickupLatitude()
                        + ", "
                        + event.getPickupLongitude()
        );

        System.out.println(
                "Destination: "
                        + event.getDestinationLatitude()
                        + ", "
                        + event.getDestinationLongitude()
        );

        // Match a driver for this ride
        rideService.matchDriverForRide(event);
    }

    @KafkaListener(
            topics = "ride-accepted",
            groupId = "ride-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeRideAccepted(
            RideAcceptedEvent event) {

        System.out.println("RideAccepted event received:");
        System.out.println("Ride ID: " + event.getRideId());
        System.out.println("Rider ID: " + event.getRiderId());
        System.out.println("Driver ID: " + event.getDriverId());

        notificationService.createNotification(
                event.getRiderId(),
                event.getRideId(),
                "Your driver has accepted the ride",
                "RIDE_ACCEPTED"
        );
    }

    @KafkaListener(
            topics = "ride-started",
            groupId = "ride-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeRideStarted(
            RideStartedEvent event) {

        System.out.println("RideStarted event received:");
        System.out.println("Ride ID: " + event.getRideId());
        System.out.println("Rider ID: " + event.getRiderId());
        System.out.println("Driver ID: " + event.getDriverId());

        notificationService.createNotification(
                event.getRiderId(),
                event.getRideId(),
                "Your ride has started",
                "RIDE_STARTED"
        );
    }

    @KafkaListener(
            topics = "ride-completed",
            groupId = "ride-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeRideCompleted(
            RideCompletedEvent event) {

        System.out.println("RideCompleted event received:");
        System.out.println("Ride ID: " + event.getRideId());
        System.out.println("Rider ID: " + event.getRiderId());
        System.out.println("Driver ID: " + event.getDriverId());
        System.out.println("Final Fare: " + event.getFinalFare());
    }

    @KafkaListener(
            topics = "payment-completed",
            groupId = "ride-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentCompleted(
            PaymentCompletedEvent event) {

        System.out.println("PaymentCompleted event received:");
        System.out.println("Ride ID: " + event.getRideId());
        System.out.println("Rider ID: " + event.getRiderId());
        System.out.println("Driver ID: " + event.getDriverId());
        System.out.println("Amount: " + event.getAmount());
    }

    @KafkaListener(
            topics = "payment-failed",
            groupId = "ride-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentFailed(
            PaymentFailedEvent event) {

        System.out.println("PaymentFailed event received:");
        System.out.println("Ride ID: " + event.getRideId());
        System.out.println("Rider ID: " + event.getRiderId());
        System.out.println("Driver ID: " + event.getDriverId());
        System.out.println("Amount: " + event.getAmount());
    }
}