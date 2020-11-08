package com.azortis.pws;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public final class PWS {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(PWS.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static File flightFile;
    private static volatile Flight currentFlight;

    public static void main(String[] args) throws Exception {
        logger.info("Starting up...");
        File directory = new File(PWS.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        logger.info("Loading settings file...");
        File settingsFile = new File(directory, "settings.json");
        if(!settingsFile.exists()){
            logger.info("Settings file does not exists! Creating one and shutting down...");
            copy(PWS.class.getResourceAsStream("settings.json"), settingsFile);
            return;
        }
        Settings settings = gson.fromJson(new FileReader(settingsFile), Settings.class);
        settings.setFlights(settings.getFlights() + 1);
        logger.info("Creating new flight with id: " + settings.getFlights());
        FlightInfo currentFlightInfo = new FlightInfo(System.currentTimeMillis(), settings.getFlights());
        File currentFlightDirectory = new File(directory + "/" + settings.getFlights());
        currentFlightDirectory.mkdir();
        File flightInfoFile = new File(currentFlightDirectory, "flight-info.json");
        Files.write(flightInfoFile.toPath(), gson.toJson(currentFlightInfo).getBytes(), StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);
        currentFlight = new Flight();
        flightFile = new File(currentFlightDirectory, "flight.json");
        Files.write(flightFile.toPath(), gson.toJson(flightFile).getBytes(), StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);

        logger.info("Starting read thread");
        Thread thread = new Thread(new ReaderThread(settings));
        thread.start();
    }

    public static void addPoint(FlightPoint flightPoint){
        currentFlight.getFlightPoints().add(flightPoint);
        saveFile(currentFlight);
    }

    private static void saveFile(Flight flight){
        try{
            final String json = gson.toJson(flight);
            flightFile.delete();
            Files.write(flightFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void copy(InputStream in, File file) {
        try (OutputStream out = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
