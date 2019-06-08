package com.poi;

import com.poi.model.Area;
import com.poi.model.POI;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Utility class
 */
public class POIUtil {
    private static final Logger LOGGER = LogManager.getLogManager().getLogger(POIService.class.getName());

    /**
     * Return a set of POIs defined within an input file
     * @param inputFile File
     * @return Set<POI></POI>
     * @throws IOException If it is impossible to read through the input file
     */
    public static Set<POI> getPOIsSet(File inputFile) throws IOException {
        if (inputFile == null || inputFile.length() == 0) {
            throw new IllegalArgumentException("Input file null or empty");
        }
        List<String> lines = Files.readAllLines(inputFile.toPath(), Charset.defaultCharset());
        if (lines.size() < 2) {
            throw new IllegalArgumentException("No POI defined in input file");
        }
        Set<POI> poisSet = new HashSet<>(); //A set is used in order to avoid duplicates
        Iterator<String> lineIterator = lines.iterator();
        //Ignore first line of the input file, which is the header
        lineIterator.next();
        while (lineIterator.hasNext()) {
            String line = lineIterator.next();
            String[] columns = line.split("\\s+");
            if (columns.length == 3) {
                try {
                    double lat = Double.parseDouble(columns[1].trim());
                    double lon = Double.parseDouble(columns[2].trim());
                    POI poi = new POI(columns[0].trim(), lat, lon);
                    poisSet.add(poi);
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Invalid lat/lon format: " + line);
                }
            } else {  //Otherwise POI definition is incomplete or invalid, thus the line must be ignored
                LOGGER.log(Level.WARNING, "Invalid line: ", line);
            }
        }
        return poisSet;
    }

    /**
     * Return the area where the input POI is located
     * @param poi POI
     * @return Area
     */
    public static Area getArea(POI poi) {
        double minLat = getMinCoordinate(poi.getLat());
        double minLon = getMinCoordinate(poi.getLon());
        return new Area(minLat, minLon);
    }

    private static double getMinCoordinate(double poiLat) {
        double minCoordinate = Math.floor(poiLat);
        if (Math.abs(poiLat - minCoordinate) >= 0.5) {
            minCoordinate += 0.5;
        }
        return minCoordinate;
    }

}
