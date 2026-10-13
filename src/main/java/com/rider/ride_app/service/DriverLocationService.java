package com.rider.ride_app.service;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverLocationService {

    private static final String DRIVER_LOCATION_KEY =
            "driver:locations";

    private static final String ONLINE_DRIVERS_KEY =
            "drivers:online";

    private final RedisTemplate<String, String> redisTemplate;

    public DriverLocationService(
            RedisTemplate<String, String> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    public void updateLocation(
            Long driverId,
            double latitude,
            double longitude) {

        Point location = new Point(longitude, latitude);

        redisTemplate.opsForGeo()
                .add(
                        DRIVER_LOCATION_KEY,
                        location,
                        String.valueOf(driverId)
                );
    }

    public List<String> findNearbyDrivers(
            double latitude,
            double longitude,
            double radiusKm) {

        GeoOperations<String, String> geoOperations =
                redisTemplate.opsForGeo();

        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                geoOperations.search(
                        DRIVER_LOCATION_KEY,
                        GeoReference.fromCoordinate(
                                longitude,
                                latitude
                        ),
                        new Distance(
                                radiusKm,
                                Metrics.KILOMETERS
                        ),
                        RedisGeoCommands.GeoSearchCommandArgs
                                .newGeoSearchArgs()
                                .includeDistance()
                                .sortAscending()
                );

        if (results == null) {
            return List.of();
        }

        return results.getContent()
                .stream()
                .map(result -> result.getContent().getName())
                .toList();
    }

    public void setDriverOnline(Long driverId) {

        redisTemplate.opsForSet()
                .add(
                        ONLINE_DRIVERS_KEY,
                        String.valueOf(driverId)
                );
    }

    public void setDriverOffline(Long driverId) {

        redisTemplate.opsForSet()
                .remove(
                        ONLINE_DRIVERS_KEY,
                        String.valueOf(driverId)
                );

        redisTemplate.opsForGeo()
                .remove(
                        DRIVER_LOCATION_KEY,
                        String.valueOf(driverId)
                );
    }

}