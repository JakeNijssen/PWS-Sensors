package com.azortis.pws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReaderThread implements Runnable{

    private final Settings settings;
    private final Process gpsProcess;
    private final BufferedReader gpsReader;

    public ReaderThread(Settings settings) throws IOException {
        this.settings = settings;
        gpsProcess = Runtime.getRuntime().exec(settings.getGpsExec());
        gpsReader = new BufferedReader(new InputStreamReader(gpsProcess.getInputStream()));
    }

    @Override
    public void run() {
        while (true){
            long startingMillis = System.currentTimeMillis();
            try {
                String gpsString = gpsReader.readLine();
                double lon = Double.parseDouble(gpsString.split("#")[0].trim().replaceFirst("lon=", ""));
                double lat = Double.parseDouble(gpsString.split("#")[1].trim().replaceFirst("lat=", ""));
                Process heightProcess = Runtime.getRuntime().exec(settings.getBarometerExec());
                String barometerString = new BufferedReader(new InputStreamReader(heightProcess.getInputStream())).readLine();
                double height = Double.parseDouble(barometerString.split("#")[0].trim().replaceFirst("height=", ""));
                double temp = Double.parseDouble(barometerString.split("#")[1].trim().replaceFirst("temp=", ""));
                PWS.addPoint(new FlightPoint(lon, lat, height, temp, startingMillis));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wait(500 - startingMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
