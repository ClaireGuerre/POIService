package com.poi;

import com.poi.model.Area;
import com.poi.model.POI;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * POI Service
 */
public class POIService {

    private final static Logger LOGGER = LogManager.getLogManager().getLogger(POIService.class.getName());


    /**
     * Return number of POIs within an area
     *
     * @param lat       min latitude of the area
     * @param lon       min longitude of the area
     * @param inputFile input file of POIs
     * @return int
     * @throws IOException              If it is impossible to read the input file
     * @throws IllegalArgumentException If input file is null or if it contains only the header
     */
    public int getNbPOI(double lat, double lon, File inputFile) throws IOException, IllegalArgumentException {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Invalid latitude input: " + lat);
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Invalid longiotude input: " + lon);
        }
        int nbPOI = 0;
        Area intputArea = new Area(lat, lon);

        //1. Parse the file and construct POIs set
        Set<POI> poisSet = POIUtil.getPOIsSet(inputFile);

        //2. Iterate through POIS list and identify whether each POI is part of the area
        Iterator<POI> poiIterator = poisSet.iterator();
        while (poiIterator.hasNext()) {
            POI poi = poiIterator.next();
            Area area = POIUtil.getArea(poi);
            if (area.equals(intputArea)) {
                nbPOI++;
            }

        }

        //3. Return the number of POIs within the input area
        return nbPOI;
    }

    /**
     * Return *firsts* firsts heaviest area of POIs declared within input file
     *
     * @param firsts    int nb of area to return
     * @param inputFile File
     * @return List<Area>
     * @throws IOException If it is impossible to read through the input file
     */
    public List<Area> getNHeaviestAreas(int firsts, File inputFile) throws IOException {
        if (firsts < 1) {
            return new ArrayList<>();
        }
        //1. Parse the file and construct POIs set
        Set<POI> poisSet = POIUtil.getPOIsSet(inputFile);

        //2. Iterate through POIs set and construct a map of area to number of POIs within it
        Map<Area, Integer> areaNbPOIMap = new HashMap<>(poisSet.size());
        Iterator<POI> poiIterator = poisSet.iterator();
        while (poiIterator.hasNext()) {
            POI poi = poiIterator.next();
            Area area = POIUtil.getArea(poi);
            if (areaNbPOIMap.containsKey(area)) {
                int previousNumber = areaNbPOIMap.get(area);
                areaNbPOIMap.put(area, previousNumber + 1);
            } else {
                areaNbPOIMap.put(area, 1);
            }
        }

        //3. Sort the map keys with values as criteria
        LinkedHashMap<Area, Integer> sortedAreaNbPOIsMap = areaNbPOIMap.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e2, LinkedHashMap::new));

        //4. Store the result in a list
        List<Area> sortedAreaNbPOIList = new LinkedList<>();
        sortedAreaNbPOIsMap.forEach((e, f) -> sortedAreaNbPOIList.add(e));

        //5. Return the *firsts* firsts list item
        return sortedAreaNbPOIList.subList(0, firsts);
    }
}


