package com.laundry.app.dto.serviceall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class ServiceAllResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<ServiceAllDto> servicesAllList;

    public List<ServiceAllDto> getServicesAllList() {
        return servicesAllList;
    }
}
