package com.rider.ride_app.service;

import com.rider.ride_app.entity.Notification;
import com.rider.ride_app.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(
            Long userId,
            Long rideId,
            String message,
            String type) {

        Notification notification = new Notification(
                userId,
                rideId,
                message,
                type
        );

        notificationRepository.save(notification);
    }
}