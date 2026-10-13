package com.rider.ride_app.dto;

import jakarta.validation.constraints.NotBlank;

public class DriverProfileRequest {

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    public DriverProfileRequest() {
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}