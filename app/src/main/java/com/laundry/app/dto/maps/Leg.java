package com.laundry.app.dto.maps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Leg implements Serializable {
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("weight")
    @Expose
    private Double weight;
    @SerializedName("duration")
    @Expose
    private Double duration;
    @SerializedName("steps")
    @Expose
    private List<Object> steps = null;
    @SerializedName("distance")
    @Expose
    private Double distance;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public List<Object> getSteps() {
        return steps;
    }

    public void setSteps(List<Object> steps) {
        this.steps = steps;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
