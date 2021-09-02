package com.laundry.app.dto.serviceall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceAllDto implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("serviceIcon")
    @Expose
    private String serviceIcon;
    @SerializedName("description")

    @Expose
    private String description;

    public ServiceAllDto(int id, String name, String serviceIcon, String description) {

        this.id = id;
        this.name = name;
        this.serviceIcon = serviceIcon;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(String serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceAllDto() {
    }
}