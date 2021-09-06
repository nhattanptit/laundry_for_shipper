package com.laundry.app.dto.maps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Waypoint implements Serializable {
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private List<Double> location = null;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }
}
