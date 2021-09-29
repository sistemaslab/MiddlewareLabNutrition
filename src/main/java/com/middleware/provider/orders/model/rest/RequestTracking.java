package com.middleware.provider.orders.model.rest;

public class RequestTracking {

    private String trackingNumber;
    private String trackingUrl;
    private String courier;

    public RequestTracking() {
    }

    public RequestTracking(String trackingNumber, String trackingUrl, String courier) {
        this.trackingNumber = trackingNumber;
        this.trackingUrl = trackingUrl;
        this.courier = courier;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

}
