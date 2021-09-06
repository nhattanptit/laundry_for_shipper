package com.laundry.app.dto.maps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MapDirectionResponse implements Serializable {
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;
    @SerializedName("waypoints")
    @Expose
    private List<Waypoint> waypoints = null;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("uuid")
    @Expose
    private String uuid;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
