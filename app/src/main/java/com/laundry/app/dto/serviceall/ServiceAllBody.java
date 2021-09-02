package com.laundry.app.dto.serviceall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ServiceAllBody implements Serializable {

    @SerializedName("data")
    @Expose
    public List<ServiceAllDto> data;
}
