package com.azortis.pws;

public class FlightPoint {

    private final double lon;
    private final double lat;
    private final double height;
    private final double temperature;
    private final long timeStamp;

    public FlightPoint(double lon, double lat, double height, double temperature, long timeStamp) {
        this.lon = lon;
        this.lat = lat;
        this.height = height;
        this.temperature = temperature;
        this.timeStamp = timeStamp;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public double getHeight() {
        return height;
    }

    public double getTemperature() {
        return temperature;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
