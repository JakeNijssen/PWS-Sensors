package com.azortis.pws;

public class FlightInfo {

    private final long startTime;
    private final long id;

    public FlightInfo(long startTime, long id) {
        this.startTime = startTime;
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getId() {
        return id;
    }
}
