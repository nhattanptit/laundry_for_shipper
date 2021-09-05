package com.laundry.app.dto.servicelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

import java.util.List;

public class ServiceListResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public List<ServiceListDto> servicesList;
}
