package com.azortis.pws;

public class Settings {

    private String gpsExec;
    private String barometerExec;
    private int flights;

    public String getGpsExec() {
        return gpsExec;
    }

    public String getBarometerExec() {
        return barometerExec;
    }

    public int getFlights() {
        return flights;
    }

    public void setFlights(int flights) {
        this.flights = flights;
    }
}
