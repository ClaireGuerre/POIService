package com.poi.model;

import java.util.Objects;

public class Area{

    private double minLat;
    private double maxLat;
    private double minLon;
    private double maxLon;

    public Area(double minLat, double maxLat, double minLon, double maxLon) {
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.maxLon = maxLon;
    }

    public Area(double minLat, double minLon) {
        this.minLat = minLat;
        this.maxLat = minLat + 0.5;
        this.minLon = minLon;
        this.maxLon = minLon + 0.5;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMaxLon() {
        return maxLon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return Double.compare(area.minLat, minLat) == 0 &&
                Double.compare(area.maxLat, maxLat) == 0 &&
                Double.compare(area.minLon, minLon) == 0 &&
                Double.compare(area.maxLon, maxLon) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(minLat, maxLat, minLon, maxLon);
    }

}
