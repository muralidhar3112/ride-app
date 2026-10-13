package com.rider.ride_app.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.rider.ride_app.event.PaymentCompletedEvent;

@Service
public class RideEventProducer {

    private static final String RIDE_REQUESTED_TOPIC =
            "ride-requested";
    private static final String RIDE_ACCEPTED_TOPIC = "ride-accepted";

    private static final String RIDE_STARTED_TOPIC = "ride-started";

    private static final String RIDE_COMPLETED_TOPIC = "ride-completed";

    private static final String PAYMENT_COMPLETED_TOPIC =
            "payment-completed";

    private static final String PAYMENT_FAILED_TOPIC =
            "payment-failed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RideEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRideRequested(
            RideRequestedEvent event) {

        kafkaTemplate.send(
                RIDE_REQUESTED_TOPIC,
                String.valueOf(event.getRideId()),
                event
        );
    }

    public void publishRideAccepted(RideAcceptedEvent event) {

        kafkaTemplate.send(
                RIDE_ACCEPTED_TOPIC,
                String.valueOf(event.getRideId()),
                event
        );
    }

    public void publishRideStarted(RideStartedEvent event) {

        kafkaTemplate.send(
                RIDE_STARTED_TOPIC,
                String.valueOf(event.getRideId()),
                event
        );
    }

    public void publishRideCompleted(
            RideCompletedEvent event) {

        kafkaTemplate.send(
                RIDE_COMPLETED_TOPIC,
                String.valueOf(event.getRideId()),
                event
        );
    }

    public void publishPaymentCompleted(
            PaymentCompletedEvent event) {

        kafkaTemplate.send(
                PAYMENT_COMPLETED_TOPIC,
                String.valueOf(event.getRideId()),
                event
        );
    }

    public void publishPaymentFailed(
            PaymentFailedEvent event) {

        kafkaTemplate.send(
                PAYMENT_FAILED_TOPIC,
                String.valueOf(event.getRideId()),
                event
        );
    }
}
