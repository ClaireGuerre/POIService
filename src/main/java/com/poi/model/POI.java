package com.poi.model;

import java.util.Objects;

public class POI {

    private String id;
    private double lat;
    private double lon;

    public POI(String id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        POI poi = (POI) o;
        return Double.compare(poi.lat, lat) == 0 &&
                Double.compare(poi.lon, lon) == 0 &&
                Objects.equals(id, poi.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lon);
    }

}
